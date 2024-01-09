package atnibam.space.common.core.exception;

import atnibam.space.common.core.enums.ResultCode;
import atnibam.space.common.core.exception.base.BaseException;

public class UserOperateException extends BaseException {
    public UserOperateException(ResultCode resultCode) {
        super(resultCode);
    }
}
