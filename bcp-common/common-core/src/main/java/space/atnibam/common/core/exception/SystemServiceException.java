package space.atnibam.common.core.exception;

import space.atnibam.common.core.enums.ResultCode;
import space.atnibam.common.core.exception.base.BaseException;

public class SystemServiceException extends BaseException {
    public SystemServiceException(ResultCode resultCode) {
        super(resultCode);
    }
}