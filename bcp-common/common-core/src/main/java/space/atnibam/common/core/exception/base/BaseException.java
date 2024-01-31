package space.atnibam.common.core.exception.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import space.atnibam.common.core.enums.ResultCode;

/**
 * @ClassName: BaseException
 * @Description: 该类是所有自定义异常的基础类，封装了业务层返回的错误码信息。
 * @Author: AtnibamAitay
 * @CreateTime: 2023-12-30 15:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {
    /**
     * 序列化版本号，遵循Serializable接口规范。
     */
    private static final long serialVersionUID = -1L;
    /**
     * 业务结果码，用于表示不同的业务异常情况。
     */
    private ResultCode resultCode;
}