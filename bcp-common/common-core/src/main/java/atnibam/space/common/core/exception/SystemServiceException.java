package atnibam.space.common.core.exception;

import atnibam.space.common.core.enums.ResultCode;
import atnibam.space.common.core.exception.base.BaseException;

public class SystemServiceException extends BaseException {
    public SystemServiceException(ResultCode resultCode) {
        super(resultCode);
    }
}