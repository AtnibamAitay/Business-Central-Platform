package space.atnibam.common.core.exception;

import space.atnibam.common.core.enums.ResultCode;
import space.atnibam.common.core.exception.base.BaseException;

/**
 * @ClassName: WxPayException
 * @Description: 微信支付异常类，继承自BaseException
 * @Author: AtnibamAitay
 * @CreateTime: 当前写注释的时间，格式为yyyy-mm-dd hh:mm
 **/
public class RedisException extends BaseException {
    /**
     * 构造方法
     *
     * @param resultCode 异常对应的结果代码
     */
    public RedisException(ResultCode resultCode) {
        super(resultCode);
    }
}