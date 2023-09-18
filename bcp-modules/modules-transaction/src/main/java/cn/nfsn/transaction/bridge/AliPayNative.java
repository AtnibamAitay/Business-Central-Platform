package cn.nfsn.transaction.bridge;

import cn.hutool.core.net.URLDecoder;
import cn.nfsn.common.core.enums.ResultCode;
import cn.nfsn.common.core.exception.AliPayException;
import cn.nfsn.transaction.config.AlipayClientConfig;
import cn.nfsn.transaction.enums.OrderStatus;
import cn.nfsn.transaction.enums.PayType;
import cn.nfsn.transaction.model.dto.AlipayBizContentDTO;
import cn.nfsn.transaction.model.dto.ProductDTO;
import cn.nfsn.transaction.model.dto.ResponseWxPayNotifyDTO;
import cn.nfsn.transaction.model.entity.OrderInfo;
import cn.nfsn.transaction.service.OrderInfoService;
import cn.nfsn.transaction.service.PaymentInfoService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.file.IOUtils;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Map;

import static cn.nfsn.transaction.constant.AliPayConstant.PRODUCT_CODE;

/**
 * @ClassName: AliPayNative
 * @Description:
 * @Author: atnibamaitay
 * @CreateTime: 2023/9/14 0014 18:21
 **/
@Slf4j
@Component
public class AliPayNative implements IPayMode {

    @Resource
    private OrderInfoService orderInfoService;

    @Resource
    private AlipayClientConfig config;

    @Resource
    private AlipayClient alipayClient;

    @Resource
    private PaymentInfoService paymentInfoService;

    /**
     * 重入锁，用于处理并发问题
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 创建订单的方法
     *
     * @param productDTO 商品数据传输对象,包含商品相关信息
     * @return 返回一个包含订单信息的Map对象
     * @throws Exception 如果在创建订单过程中出现问题，将抛出异常
     */
    @Override
    public Object createOrder(ProductDTO productDTO) throws Exception {
        try {
            // 生成订单
            log.info("生成订单");
            OrderInfo orderInfo = orderInfoService.createOrderByProductId(productDTO, PayType.ALIPAY.getType());

            //获取订单二维码URL
            String codeUrl = orderInfo.getCodeUrl();

            //检查订单是否存在且二维码URL是否已保存
            if (orderInfo != null && !StringUtils.isEmpty(codeUrl)) {
                // 添加订单号到日志
                log.info("订单：{} 已存在，二维码已保存", orderInfo.getOrderNo());

                //返回二维码和订单号
                return codeUrl;
            }

            // 调用支付宝接口
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

            // 配置需要的公共请求参数
            // 支付完成后，支付宝向应用发起异步通知的地址
            request.setNotifyUrl(config.getNotifyUrl());

            // 支付完成后，我们想让页面跳转回应用的页面，配置returnUrl
            request.setReturnUrl(config.getReturnUrl());

            // 组装当前业务方法的请求参数
            // 使用建造者模式创建 AlipayBizContentDTO 对象
            AlipayBizContentDTO alipayBizContentDTO = AlipayBizContentDTO.builder()
                    .outTradeNo(orderInfo.getOrderNo())
                    .totalAmount(new BigDecimal(orderInfo.getTotalFee().toString()).divide(new BigDecimal(100)))
                    .subject(orderInfo.getTitle())
                    .productCode(PRODUCT_CODE)
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            // 将Java对象转换为JSON格式的字符串
            String jsonString = objectMapper.writeValueAsString(alipayBizContentDTO);
            System.out.println("==================TEST: " + jsonString);
            request.setBizContent(jsonString.toString());

            // 执行请求，调用支付宝接口
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);

            if(response.isSuccess()){
                log.info("调用成功，返回结果 ===> " + response.getBody());
                //保存结果
                codeUrl = response.getBody();
                String orderNo = orderInfo.getOrderNo();
                orderInfoService.saveCodeUrl(orderNo, codeUrl);

                return response.getBody();
            } else {
                log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
                throw new RuntimeException("创建支付交易失败");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("创建支付交易失败");
        }
    }

    /**
     * 处理支付通知的方法。
     *
     * @param request        HttpServletRequest对象，用于获取请求参数等信息
     * @param successStatus  订单成功状态
     * @return               返回值为 ResponseWxPayNotifyDTO 对象
     * @throws IOException              如果从request中读取参数出现异常
     * @throws GeneralSecurityException 如果在进行签名验证时出现异常
     */
    @Override
    public ResponseWxPayNotifyDTO handlePaymentNotification(HttpServletRequest request, OrderStatus successStatus) throws IOException, GeneralSecurityException {
        Map<String, String> params = parseParamsFromRequest(request);
        validateSign(params);
        String outTradeNo = getStringParam(params, "out_trade_no");
        OrderInfo order = getAndValidateOrder(outTradeNo);
        validateAmount(params, order);
        validateSellerId(params);
        validateAppId(params);
        validateTradeStatus(params);

        Map<String, Object> paramsObject = new HashMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramsObject.put(entry.getKey(), entry.getValue());
        }

        processOrder(paramsObject, successStatus);
        return null;
    }

    /**
     * 从请求中解析参数。
     *
     * @param request HttpServletRequest对象，用于获取请求参数等信息
     * @return        返回值为一个Map，键和值都是字符串类型
     * @throws IOException 如果从request中读取参数出现异常
     */
    private Map<String, String> parseParamsFromRequest(HttpServletRequest request) throws IOException {
        String paramsStr = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
        try {
            return Arrays.stream(paramsStr.split("&"))
                    .map(this::splitQueryParameter)
                    .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing params: " + paramsStr, e);
        }
    }

    /**
     * 验证签名是否正确。
     *
     * @param params 包含了所有需要验证的参数
     * @throws GeneralSecurityException 如果在进行签名验证时出现异常
     */
    private void validateSign(Map<String, String> params) throws GeneralSecurityException {
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    params,
                    config.getAlipayPublicKey(),
                    AlipayConstants.CHARSET_UTF8,
                    AlipayConstants.SIGN_TYPE_RSA2);
            if (!signVerified) {
                throw new AliPayException(ResultCode.CREATE_ORDER_CONTRAST_LOCK_FAIL);
            }
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取并验证订单信息。
     *
     * @param outTradeNo 订单编号
     * @return           返回值为 OrderInfo 对象
     */
    private OrderInfo getAndValidateOrder(String outTradeNo) {
        OrderInfo order = orderInfoService.getOrderByOrderNo(outTradeNo);
        if(order == null){
            throw new AliPayException(ResultCode.ORDER_NOT_EXIST);
        }
        return order;
    }

    /**
     * 验证支付金额是否正确。
     *
     * @param params 包含了所有需要验证的参数
     * @param order  订单信息
     */
    private void validateAmount(Map<String, String> params, OrderInfo order) {
        String totalAmount = getStringParam(params, "total_amount");
        int totalAmountInt = new BigDecimal(totalAmount).multiply(new BigDecimal("100")).intValue();
        int totalFeeInt = order.getTotalFee().intValue();
        if(totalAmountInt != totalFeeInt){
            throw new AliPayException(ResultCode.CREATE_ORDER_FAIL);
        }
    }

    /**
     * 验证销售商ID是否正确。
     *
     * @param params 包含了所有需要验证的参数
     */
    private void validateSellerId(Map<String, String> params) {
        String sellerId = getStringParam(params, "seller_id");
        if(!sellerId.equals(config.getSellerId())){
            throw new AliPayException(ResultCode.INSERT_ORDER_FAIL);
        }
    }

    /**
     * 验证应用ID是否正确。
     *
     * @param params 包含了所有需要验证的参数
     */
    private void validateAppId(Map<String, String> params) {
        String appId = getStringParam(params, "app_id");
        if(!appId.equals(config.getAppId())){
            throw new AliPayException(ResultCode.PRODUCT_OR_PAY_TYPE_NULL);
        }
    }

    /**
     * 验证交易状态是否正确。
     *
     * @param params 包含了所有需要验证的参数
     */
    private void validateTradeStatus(Map<String, String> params) {
        String tradeStatus = getStringParam(params, "trade_status");
        if(!"TRADE_SUCCESS".equals(tradeStatus)){
            throw new AliPayException(ResultCode.ORDER_PAYING);
        }
    }

    /**
     * 获取指定键的参数值。
     *
     * @param params 包含了所有需要验证的参数
     * @param key    需要获取值的键
     * @return       返回对应键的值，如果没有找到则抛出异常
     */
    private String getStringParam(Map<String, String> params, String key) {
        String value = params.get(key);
        if (value == null) {
            throw new RuntimeException("Missing required param: " + key);
        }
        return value;
    }


    /**
     * 将查询参数字符串分割为键值对.
     *
     * @param it 查询参数字符串
     * @return 键值对
     */
    private AbstractMap.SimpleEntry<String, String> splitQueryParameter(String it) {
        final int idx = it.indexOf("=");
        final String key = idx > 0 ? it.substring(0, idx) : it;
        final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
        return new AbstractMap.SimpleEntry<>(
                URLDecoder.decode(key, StandardCharsets.UTF_8),
                URLDecoder.decode(value, StandardCharsets.UTF_8)
        );
    }

    /**
     * 将Map的值转化为字符串类型.
     *
     * @param params 参数
     * @return 转换后的Map
     */
    private Map<String, String> convertParamsToStringKey(Map<String, Object> params) {
        return params.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue() != null ? e.getValue().toString() : ""
                ));
    }

    /**
     * 处理订单
     *
     * @param bodyMap 请求体Map
     * @throws GeneralSecurityException 抛出安全异常
     */
    @Override
    public void processOrder(Map<String, Object> bodyMap, OrderStatus successStatus) throws GeneralSecurityException {
        log.info("开始处理订单");

        Object orderNoObj = bodyMap.get("out_trade_no");
        if (!(orderNoObj instanceof String)) {
            throw new RuntimeException("Unexpected type for 'out_trade_no': " + (orderNoObj == null ? "null" : orderNoObj.getClass()));
        }

        // 从参数中获取订单号
        String orderNo = bodyMap.get("out_trade_no").toString();

        /*
         * 尝试获取锁：
         * 如果成功获取则立即返回true，获取失败则立即返回false。
         * 不必一直等待锁的释放
         */
        if(lock.tryLock()) {
            try {

                /*
                 * 处理重复通知
                 * 接口调用的幂等性：无论接口被调用多少次，以下业务只执行一次
                 */
                String orderStatus = orderInfoService.getOrderStatus(orderNo);
                if (!OrderStatus.NOTPAY.getType().equals(orderStatus)) {
                    return;
                }

                // 更新订单状态为成功
                orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);

                Map<String, String> bodyMapString = convertParamsToStringKey(bodyMap);

                // 记录支付日志
                paymentInfoService.createPaymentInfoForAliPay(bodyMapString);

            } finally {
                // 处理完毕后，需要主动释放锁
                lock.unlock();
            }
        }
    }

    /**
     * 取消订单
     *
     * @param orderNo 订单号
     * @throws Exception 抛出异常
     */
    @Override
    public void cancelOrder(String orderNo) throws Exception {

    }

    /**
     * 根据订单号和退款原因进行退款操作
     *
     * @param orderNo 订单编号，不能为空
     * @param reason  退款原因，不能为空
     * @throws Exception 若退款过程中发生错误，则抛出异常
     */
    @Override
    public void refund(String orderNo, String reason) throws Exception {

    }

    /**
     * 处理退款单
     *
     * @param bodyMap 请求体Map，包含了支付宝通知的退款信息
     * @throws Exception 抛出异常，包括但不限于解密错误、数据库操作失败等
     */
    @Override
    public void processRefund(Map<String, Object> bodyMap, OrderStatus successStatus) throws Exception {

    }
}