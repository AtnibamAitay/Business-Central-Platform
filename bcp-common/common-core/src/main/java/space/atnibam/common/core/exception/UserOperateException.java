package space.atnibam.common.core.exception;

import space.atnibam.common.core.enums.ResultCode;
import space.atnibam.common.core.exception.base.BaseException;

public class UserOperateException extends BaseException {
    public UserOperateException(ResultCode resultCode) {
        super(resultCode);
    }
}
