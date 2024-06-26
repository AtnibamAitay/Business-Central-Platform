package space.atnibam.transaction.service.impl;

import space.atnibam.transaction.enums.OrderStatus;
import space.atnibam.transaction.mapper.OrderInfoMapper;
import space.atnibam.transaction.model.dto.ProductDTO;
import space.atnibam.transaction.model.entity.App;
import space.atnibam.transaction.model.entity.OrderInfo;
import space.atnibam.transaction.service.AppService;
import space.atnibam.transaction.service.OrderInfoService;
import space.atnibam.transaction.utils.OrderNoUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import space.atnibam.common.core.exception.WxPayException;
import space.atnibam.transaction.constant.OrderConstant;
import space.atnibam.transaction.constant.RabbitConstant;

import javax.annotation.Resource;

import static space.atnibam.common.core.enums.ResultCode.*;


/**
 * @ClassName: OrderInfoServiceImpl
 * @Description: 订单相关服务的实现类，包括订单的生成、更新状态、查询等功能
 * @Author: atnibamaitay
 * @CreateTime: 2023-08-31 23:04
 **/
@Service
@Slf4j
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
        implements OrderInfoService {

    @Resource
    private AppService appService;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RabbitTemplate rabbitTemplate;


    /**
     * 创建订单
     *
     * @param productDTO  商品信息
     * @param paymentType 支付类型
     * @return 返回新建的或者已存在但未支付的订单
     * @throws InterruptedException 获取分布式锁时可能会抛出此异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderInfo createOrderByProductId(ProductDTO productDTO, String paymentType) throws InterruptedException {

        // 校验参数
        if (productDTO == null || StringUtils.isEmpty(paymentType)) {
            throw new WxPayException(PRODUCT_OR_PAY_TYPE_NULL);
        }

        // 校验 App
        App app = appService.validPayApp(productDTO.getAppId());

        // 查找商品id对应的已存在但未支付的订单
        OrderInfo orderInfo = this.getNoPayOrderByProductId(productDTO.getAppId(), productDTO.getUserId(), paymentType);

        if (orderInfo != null) {
            log.warn("订单已存在，订单号：{}", orderInfo.getOrderNo());
            return orderInfo;
        }

        //指定锁的名称，将锁的粒度为订单级别
        String lockName = OrderConstant.ORDER_LOCK_PREFIX + productDTO.getAppId() + productDTO.getUserId();
        //获取锁
        RLock lock = redissonClient.getLock(lockName);

        log.info("开始尝试获取锁: {}", lockName);

        //尝试获取锁，参数分别是：获取锁的最大等待时间(期间会重试)，锁自动释放时间，时间单位
        boolean isLock = lock.tryLock(OrderConstant.MAX_WAIT_TIME, OrderConstant.EXPIRE_TIME, OrderConstant.TIME_UNIT);

        //判断获取锁成功
        if (isLock) {
            try {
                log.info("成功获取到锁: {}", lockName);

                // 生成新的订单
                orderInfo = OrderInfo.builder()
                        .title(productDTO.getTitle())
                        // 设置订单号
                        .orderNo(OrderNoUtils.getOrderNo())
                        .productId(productDTO.getId())
                        // 设置订单金额
                        .totalFee(productDTO.getPrice())
                        // 设置订单状态为未支付
                        .orderStatus(OrderStatus.NOTPAY.getType())
                        .paymentType(paymentType)
                        // 设置用户id
                        .userId(productDTO.getUserId())
                        // 设置退款回调地址refundCallbackUrl
                        .refundCallBackUrl(app.getRefundCallBackUrl())
                        // 设置支付应用id
                        .appId(app.getId())
                        // 设置支付结果回调地址
                        .paymentCallBackUrl(app.getPaymentCallBackUrl())
                        .build();

                //插入数据
                int insertResult = baseMapper.insert(orderInfo);
                if (insertResult <= 0) {
                    throw new WxPayException(INSERT_ORDER_FAIL);
                }

                // 使用MQ设置订单超时时间，如果30分钟内，订单状态没有变成OrderStatus.SUCCESS，则30分钟超时后将订单状态设置为OrderStatus.CLOSED
                // 发送延迟消息到RabbitMQ
                rabbitTemplate.convertAndSend(
                        RabbitConstant.ORDER_EXCHANGE,
                        RabbitConstant.ORDER_QUEUE_ROUTING_KEY,
                        orderInfo.getOrderNo()
                );
                log.info("订单创建成功，并已发送延迟消息，订单号：{}", orderInfo.getOrderNo());
            } finally {
                // 判断当前线程是否持有锁
                if (lock.isHeldByCurrentThread()) {
                    //释放锁
                    lock.unlock();
                    log.info("锁释放成功: {}", lockName);
                } else {
                    throw new WxPayException(CREATE_ORDER_CONTRAST_LOCK_FAIL);
                }
            }
            return orderInfo;
        }
        log.warn("未获得锁，订单创建失败: {}", lockName);

        // 未获取到锁，抛出异常
        throw new WxPayException(CREATE_ORDER_FAIL);
    }

    /**
     * 查询未支付订单
     * 防止在同一个APP中重复创建订单对象
     *
     * @param appId       AppId
     * @param userId      用户id
     * @param paymentType 支付类型
     * @return 返回查询到的订单，如果没有则返回null
     */
    private OrderInfo getNoPayOrderByProductId(Integer appId, Long userId, String paymentType) {

        // 参数校验
        if (appId == null || userId == null || paymentType == null || paymentType.isEmpty()) {
            throw new WxPayException(PARAM_IS_BLANK);
        }

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(OrderConstant.APP_ID, appId);
        queryWrapper.eq(OrderConstant.ORDER_STATUS, OrderStatus.NOTPAY.getType());
        queryWrapper.eq(OrderConstant.USER_ID, userId);
        queryWrapper.eq(OrderConstant.PAYMENT_TYPE, paymentType);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 存储订单二维码
     *
     * @param orderNo 订单号
     * @param codeUrl 二维码地址
     */
    @Override
    public void saveCodeUrl(String orderNo, String codeUrl) {
        // TODO: 并发问题：如果有多个线程同时调用这个函数更新同一个订单的二维码，可能导致数据不一致的问题。可以考虑使用乐观锁或悲观锁来解决这个问题。

        try {
            // 使用UpdateWrapper仅更新codeUrl字段
            UpdateWrapper<OrderInfo> updateWrapper = new UpdateWrapper<>();

            updateWrapper.eq(OrderConstant.ORDER_NO, orderNo).set(OrderConstant.PAYMENT_DATA, codeUrl);

            // 执行更新操作，并验证结果
            int rowsAffected = baseMapper.update(null, updateWrapper);

            // 如果没有行受到影响，则抛出异常
            if (rowsAffected == 0) {
                throw new IllegalArgumentException("保存微信Native支付二维码失败，订单编号为: " + orderNo);
            }
        } catch (Exception e) {
            // 捕获并处理可能的异常
            log.error("更新订单号为{}的codeUrl时发生错误", orderNo, e);
            throw e;
        }
    }

    /**
     * 根据订单号获取订单状态
     *
     * @param orderNo 订单号，不能为空
     * @return 返回该订单号对应的订单状态，如果订单不存在，则返回null
     */
    @Override
    public String getOrderStatus(String orderNo) {
        // 构建查询条件
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(OrderConstant.ORDER_NO, orderNo);

        // 使用查询条件进行数据库查询
        OrderInfo orderInfo = baseMapper.selectOne(queryWrapper);

        // 判断查询结果是否为空
        if (orderInfo == null) {
            return null;
        }

        // 返回订单状态
        return orderInfo.getOrderStatus();
    }

    /**
     * 根据订单编号更新订单状态
     *
     * @param orderNo     需要更新的订单的订单编号
     * @param orderStatus 更新后的订单状态
     */
    @Override
    public void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus) {
        // 记录日志，输出更新后的订单状态
        log.info("更新订单状态 ===> {}", orderStatus.getType());

        // 创建查询条件
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        // 添加查询条件：订单编号等于参数中的订单编号
        queryWrapper.eq("order_no", orderNo);

        // 创建新的订单信息对象，并设置新的订单状态
        OrderInfo orderInfo = OrderInfo.builder()
                .orderStatus(orderStatus.getType())
                .build();

        // 根据查询条件，更新订单状态
        baseMapper.update(orderInfo, queryWrapper);
    }

    /**
     * 根据订单号获取订单
     *
     * @param orderNo 订单号
     * @return 返回查询到的订单
     */
    @Override
    public OrderInfo getOrderByOrderNo(String orderNo) {

        // 创建查询条件封装对象
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();

        // 添加查询条件，要求订单编号字段等于传入的订单编号
        queryWrapper.eq(OrderConstant.ORDER_NO, orderNo);

        // 执行查询操作，查询结果最多有一条记录，因为订单编号是唯一的
        OrderInfo orderInfo = baseMapper.selectOne(queryWrapper);

        // 返回查询到的订单信息
        return orderInfo;
    }

}