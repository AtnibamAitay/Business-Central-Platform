package space.atnibam.transaction.bridge;

import space.atnibam.common.core.enums.ResultCode;
import space.atnibam.common.core.exception.AliPayException;
import space.atnibam.transaction.config.AlipayClientConfig;
import space.atnibam.transaction.enums.AliPayTradeState;
import space.atnibam.transaction.enums.OrderStatus;
import space.atnibam.transaction.enums.PayType;
import space.atnibam.transaction.model.dto.AlipayBizContentDTO;
import space.atnibam.transaction.model.dto.ProductDTO;
import space.atnibam.transaction.model.dto.ResponsePayNotifyDTO;
import space.atnibam.transaction.model.entity.OrderInfo;
import space.atnibam.transaction.model.entity.RefundInfo;
import space.atnibam.transaction.service.OrderInfoService;
import space.atnibam.transaction.service.PaymentInfoService;
import space.atnibam.transaction.service.RefundInfoService;
import space.atnibam.transaction.utils.AliPayUtils;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import space.atnibam.transaction.constant.AliPayConstant;
import space.atnibam.transaction.constant.OrderConstant;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: AliPayNative
 * @Description: 本类主要用于处理与支付宝原生支付（AliPayNative）有关的业务逻辑，包括创建订单、处理支付通知、取消订单以及退款等功能。
 * 对外提供统一的接口，以规范支付流程和实现支付功能。具体实现了 IPayMode 接口中定义的各类方法。
 * 使用阿里支付宝SDK进行API调用，实现与支付宝的交互，包括签名验证、支付请求发送等操作。
 * @Author: atnibamaitay
 * @CreateTime: 2023/9/14 0014 18:21
 **/
@Slf4j
@Component
public class AliPayNative implements IPayMode {

    /**
     * 重入锁，用于处理并发问题
     */
    private final ReentrantLock lock = new ReentrantLock();
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private AlipayClientConfig config;
    @Resource
    private AlipayClient alipayClient;
    @Resource
    private PaymentInfoService paymentInfoService;
    @Resource
    private RefundInfoService refundInfoService;

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
            String paymentFormCode = orderInfo.getPaymentData();

            //检查订单是否存在且二维码URL是否已保存
            if (orderInfo != null && !StringUtils.isEmpty(paymentFormCode)) {
                // 添加订单号到日志
                log.info("订单：{} 已存在，二维码已保存", orderInfo.getOrderNo());

                //返回二维码
                return paymentFormCode;
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
                    .productCode(AliPayConstant.PRODUCT_CODE)
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            // 将Java对象转换为JSON格式的字符串
            String jsonString = objectMapper.writeValueAsString(alipayBizContentDTO);
            request.setBizContent(jsonString.toString());

            // 执行请求，调用支付宝接口
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);

            if (response.isSuccess()) {
                log.info("调用成功，返回结果 ===> " + response.getBody());
                //保存结果
                paymentFormCode = response.getBody();
                String orderNo = orderInfo.getOrderNo();
                orderInfoService.saveCodeUrl(orderNo, paymentFormCode);

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
     * 处理支付宝支付通知
     *
     * @param request       当前请求
     * @param successStatus 成功状态
     * @return 响应支付通知的DTO
     * @throws IOException              如果解析请求或读取内容出现问题，抛出IO异常
     * @throws GeneralSecurityException 如果签名验证失败，抛出安全异常
     */
    @Override
    public ResponsePayNotifyDTO paymentNotificationHandler(HttpServletRequest request, OrderStatus successStatus) throws IOException, GeneralSecurityException {
        // 从请求中解析参数
        Map<String, String> params = AliPayUtils.parseParamsFromRequest(request);

        // 验证签名
        validateSign(params);

        // 获取并存储订单号
        String outTradeNo = AliPayUtils.getStringParam(params, AliPayConstant.OUT_TRADE_NO);

        // 获取并验证订单信息
        OrderInfo order = getAndValidateOrder(outTradeNo);

        // 验证金额
        validateAmount(params, order);

        // 验证卖家ID
        validateSellerId(params);

        // 验证应用ID
        validateAppId(params);

        // 验证交易状态
        validateTradeStatus(params);

        // 将解析后的参数转为对象存储
        Map<String, Object> paramsObject = new HashMap<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            paramsObject.put(entry.getKey(), entry.getValue());
        }

        // 处理订单
        processOrder(paramsObject, successStatus);

        return null;
    }

    /**
     * 验证签名是否正确。
     *
     * @param params 包含了所有需要验证的参数
     */
    private void validateSign(Map<String, String> params) {
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
     * @return 返回值为 OrderInfo 对象
     */
    private OrderInfo getAndValidateOrder(String outTradeNo) {
        OrderInfo order = orderInfoService.getOrderByOrderNo(outTradeNo);
        if (order == null) {
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
        String totalAmount = AliPayUtils.getStringParam(params, AliPayConstant.TOTAL_AMOUNT);
        int totalAmountInt = new BigDecimal(totalAmount).multiply(new BigDecimal("100")).intValue();
        int totalFeeInt = order.getTotalFee().intValue();
        if (totalAmountInt != totalFeeInt) {
            throw new AliPayException(ResultCode.CREATE_ORDER_FAIL);
        }
    }

    /**
     * 验证销售商ID是否正确。
     *
     * @param params 包含了所有需要验证的参数
     */
    private void validateSellerId(Map<String, String> params) {
        String sellerId = AliPayUtils.getStringParam(params, AliPayConstant.SELLER_ID);
        if (!sellerId.equals(config.getSellerId())) {
            throw new AliPayException(ResultCode.INSERT_ORDER_FAIL);
        }
    }

    /**
     * 验证应用ID是否正确。
     *
     * @param params 包含了所有需要验证的参数
     */
    private void validateAppId(Map<String, String> params) {
        String appId = AliPayUtils.getStringParam(params, OrderConstant.APP_ID);
        if (!appId.equals(config.getAppId())) {
            throw new AliPayException(ResultCode.PRODUCT_OR_PAY_TYPE_NULL);
        }
    }

    /**
     * 验证交易状态是否正确。
     *
     * @param params 包含了所有需要验证的参数
     */
    private void validateTradeStatus(Map<String, String> params) {
        String tradeStatus = AliPayUtils.getStringParam(params, AliPayConstant.TRADE_STATUS);
        if (!"TRADE_SUCCESS".equals(tradeStatus)) {
            throw new AliPayException(ResultCode.ORDER_PAYING);
        }
    }

    /**
     * 处理订单
     *
     * @param bodyMap 请求体Map
     * @throws GeneralSecurityException 抛出安全异常
     */
    private void processOrder(Map<String, Object> bodyMap, OrderStatus successStatus) throws GeneralSecurityException {
        log.info("开始处理订单");

        Object orderNoObj = bodyMap.get(AliPayConstant.OUT_TRADE_NO);
        if (!(orderNoObj instanceof String)) {
            throw new RuntimeException("Unexpected type for 'out_trade_no': " + (orderNoObj == null ? "null" : orderNoObj.getClass()));
        }

        // 从参数中获取订单号
        String orderNo = bodyMap.get(AliPayConstant.OUT_TRADE_NO).toString();

        /*
         * 尝试获取锁：
         * 如果成功获取则立即返回true，获取失败则立即返回false。
         * 不必一直等待锁的释放
         */
        if (lock.tryLock()) {
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
                orderInfoService.updateStatusByOrderNo(orderNo, successStatus);

                // 更新支付日志
                Map<String, String> bodyMapString = AliPayUtils.convertParamsToStringKey(bodyMap);

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
     */
    @Override
    public void cancelOrder(String orderNo) {
        //调用支付宝提供的统一收单交易关闭接口
        this.closeOrder(orderNo);

        //更新用户订单状态
        orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.CANCEL);
    }

    /**
     * 关闭订单
     *
     * @param orderNo 订单号
     */
    private void closeOrder(String orderNo) {

        // 日志记录开始调用关单接口信息，输出订单号
        log.info("关闭订单接口被调用，订单号 ===> {}", orderNo);

        try {
            // 创建支付宝关闭交易请求对象
            AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();

            // 创建业务请求参数的Json对象
            JSONObject bizContent = new JSONObject();

            // 设置业务请求参数：订单号
            bizContent.put(AliPayConstant.OUT_TRADE_NO, orderNo);

            // 将业务请求参数设置到请求对象中
            request.setBizContent(bizContent.toString());

            // 发送请求，获取支付宝返回的关闭交易响应对象
            AlipayTradeCloseResponse response = alipayClient.execute(request);

            // 判断响应结果是否成功
            if (response.isSuccess()) {
                // 日志记录成功调用关单接口的返回信息
                log.info("调用成功，返回结果 ===> " + response.getBody());
            } else {
                // 日志记录失败调用关单接口的返回错误码和错误信息
                log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
                // 当调用失败时，抛出运行时异常
                throw new RuntimeException("关单接口的调用失败");
            }
        } catch (AlipayApiException e) {
            // 打印堆栈信息，并抛出运行时异常
            e.printStackTrace();
            throw new RuntimeException("关单接口的调用失败");
        }
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
        try {
            log.info("调用退款API");

            // 创建退款单
            RefundInfo refundInfo = refundInfoService.createRefundByOrderNo(orderNo, reason);

            // 调用统一收单交易退款接口
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

            // 组装当前业务方法的请求参数
            JSONObject bizContent = new JSONObject();

            // 订单编号
            bizContent.put(AliPayConstant.OUT_TRADE_NO, orderNo);
            BigDecimal refund = new BigDecimal(refundInfo.getRefund().toString()).divide(new BigDecimal("100"));

            // 退款金额：不能大于支付金额
            bizContent.put(AliPayConstant.REFUND_AMOUNT, refund);

            // 退款原因(可选)
            bizContent.put(AliPayConstant.REFUND_REASON, reason);

            request.setBizContent(bizContent.toString());

            // 执行请求，调用支付宝接口
            AlipayTradeRefundResponse response = alipayClient.execute(request);

            if (response.isSuccess()) {
                log.info("调用成功，返回结果 ===> " + response.getBody());

                // 更新订单状态
                orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_SUCCESS);

                // 更新退款单, 退款成功
                refundInfoService.updateRefundForAliPay(
                        refundInfo.getRefundNo(),
                        response.getBody(),
                        AliPayTradeState.REFUND_SUCCESS.getType()
                );
            } else {
                log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());

                // 更新订单状态
                orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_ABNORMAL);

                // 更新退款单, 退款失败
                refundInfoService.updateRefundForAliPay(
                        refundInfo.getRefundNo(),
                        response.getBody(),
                        AliPayTradeState.REFUND_ERROR.getType()
                );
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new RuntimeException("创建退款申请失败");
        }
    }
}