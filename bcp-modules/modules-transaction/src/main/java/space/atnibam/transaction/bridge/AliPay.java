package space.atnibam.transaction.bridge;

import space.atnibam.transaction.enums.OrderStatus;
import space.atnibam.transaction.model.dto.ProductDTO;
import space.atnibam.transaction.model.dto.ResponsePayNotifyDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @ClassName: AliPay
 * @Description: 支付宝支付类，继承自Pay类，实现了具体的支付逻辑
 * @Author: atnibamaitay
 * @CreateTime: 2023-09-13 17:57
 **/
@Component("aliPay")
public class AliPay extends AbstractPay {

    /**
     * 构造函数，初始化支付模式
     *
     * @param payMode 支付模式接口
     */
    public AliPay(@Qualifier("aliPayNative") IPayMode payMode) {
        super(payMode);
    }

    /**
     * 创建订单
     *
     * @param productDTO 商品信息
     * @return           包含支付宝支付的HTML表单代码的Map
     * @throws Exception 抛出异常
     */
    @Override
    public Object createOrder(ProductDTO productDTO) throws Exception {
        return payMode.createOrder(productDTO);
    }

    /**
     * 处理来自支付宝的支付通知
     *
     * @param request                    HttpServletRequest 对象，表示一个 HTTP 请求
     * @param successStatus              订单状态
     * @return ResponsePayNotifyDTO      响应对象，包含响应码和信息
     * @throws IOException               如果读取请求数据时出错
     * @throws GeneralSecurityException  如果在验证签名过程中出现安全异常
     */
    @Override
    public ResponsePayNotifyDTO paymentNotificationHandler(HttpServletRequest request, OrderStatus successStatus) throws IOException, GeneralSecurityException {
        return payMode.paymentNotificationHandler(request, successStatus);
    }

    /**
     * 取消订单
     *
     * @param orderNo    订单号
     * @throws Exception 抛出异常
     */
    @Override
    public void cancelOrder(String orderNo) throws Exception {
        payMode.cancelOrder(orderNo);
    }

    /**
     * 退款
     *
     * @param orderNo    订单编号，不能为空
     * @param reason     退款原因，不能为空
     * @throws Exception 若退款过程中发生错误，则抛出异常
     */
    @Override
    public void refund(String orderNo, String reason) throws Exception {
        payMode.refund(orderNo, reason);
    }
}