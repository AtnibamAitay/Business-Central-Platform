package atnibam.space.system.service;

import atnibam.space.common.core.domain.dto.VerifyCodeDTO;

/**
 * @Author: gaojianjie
 * @Description
 * @date 2023/9/12 18:58
 */
public interface CodeService {
    void sendCode(VerifyCodeDTO verifyCodeDTO);
}
