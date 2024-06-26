package space.atnibam.transaction.bridge;

import space.atnibam.transaction.enums.OrderStatus;
import space.atnibam.transaction.model.dto.ProductDTO;
import space.atnibam.transaction.model.dto.ResponsePayNotifyDTO;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @ClassName: IPayMode
 * @Description: 支付方式的接口，定义了创建订单的方法
 * @Author: atnibamaitay
 * @CreateTime: 2023-09-13 22:15
 **/
public interface IPayMode {

   /**
    * 创建订单
    *
    * @param productDTO 商品数据传输对象,包含商品相关信息
    * @return           返回一个包含订单信息的Map对象
    * @throws Exception 如果在创建订单过程中出现问题，将抛出异常
    */
   Object createOrder(ProductDTO productDTO) throws Exception;

    /**
     * 处理来自支付系统方的支付通知
     *
     * @param request                    HttpServletRequest 对象，表示一个 HTTP 请求
     * @param successStatus              订单状态
     * @return ResponsePayNotifyDTO      响应对象，包含响应码和信息
     * @throws IOException               如果读取请求数据时出错
     * @throws GeneralSecurityException  如果在验证签名过程中出现安全异常
     */
   ResponsePayNotifyDTO paymentNotificationHandler(HttpServletRequest request, OrderStatus successStatus) throws IOException, GeneralSecurityException;

   /**
    * 取消订单
    *
    * @param orderNo    订单号
    * @throws Exception 抛出异常
    */
    void cancelOrder(String orderNo) throws Exception;

    /**
     * 退款
     *
     * @param orderNo    订单编号，不能为空
     * @param reason     退款原因，不能为空
     * @throws Exception 若退款过程中发生错误，则抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    void refund(String orderNo, String reason) throws Exception;
}