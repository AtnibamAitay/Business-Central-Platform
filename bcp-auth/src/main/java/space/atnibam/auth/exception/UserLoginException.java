package space.atnibam.auth.exception;

import space.atnibam.common.core.enums.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: UserLoginException
 * @Description: 继承自RuntimeException的用户登录异常类，主要用于处理和用户登录相关的异常情况
 * @Author: AtnibamAitay
 * @CreateTime: 2023-11-19 14:22
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class UserLoginException extends RuntimeException {
    /**
     * 结果状态码，用来标记用户操作的结果
     */
    private ResultCode resultCode;

    /**
     * 构造方法，传入结果状态码进行实例化
     *
     * @param resultCode 结果状态码
     */
    public UserLoginException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
