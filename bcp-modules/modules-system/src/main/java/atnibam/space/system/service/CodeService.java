package atnibam.space.system.service;

import atnibam.space.common.core.domain.dto.VerifyCodeDTO;

public interface CodeService {
    void sendCode(VerifyCodeDTO verifyCodeDTO);
}
