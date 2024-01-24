package atnibam.space.system.service;

import atnibam.space.common.core.domain.R;
import atnibam.space.system.model.dto.AccountVerificationDTO;

public interface CodeService {

    /**
     * 发送验证码
     *
     * @param accountVerificationDTO 包含账号、验证码类型等信息的数据传输对象
     * @return 返回验证码发送结果
     */
    R sendCode(AccountVerificationDTO accountVerificationDTO);
}