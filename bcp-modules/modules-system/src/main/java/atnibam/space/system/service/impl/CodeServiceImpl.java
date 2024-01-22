package atnibam.space.system.service.impl;

import atnibam.space.common.core.constant.Constants;
import atnibam.space.common.core.domain.dto.VerifyCodeDTO;
import atnibam.space.common.core.enums.CertificateMethodEnum;
import atnibam.space.system.factory.CertificateStrategyFactory;
import atnibam.space.system.service.CodeService;
import atnibam.space.system.strategy.CertificateStrategy;
import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CodeService实现类
 */
@Service
public class CodeServiceImpl implements CodeService {
    @Autowired
    private CertificateStrategyFactory certificateStrategyFactory;

    /**
     * 发送验证码
     *
     * @param verifyCodeDTO 验证码信息
     */
    @Override
    public void sendCode(VerifyCodeDTO verifyCodeDTO) {
        // 生成验证码
        String code = RandomUtil.randomNumbers(Constants.RANDOM_LENGTH);
        // 发送目标
        String account = verifyCodeDTO.getAccount();
        // 主题（手机号登录则为短信主题，邮箱登录则为邮件标题）
        String subject = verifyCodeDTO.getSubject();
        // 发送验证码
        CertificateStrategy strategy = certificateStrategyFactory.getStrategy(CertificateMethodEnum.fromCode(verifyCodeDTO.getIdentity()));
        strategy.sendCodeHandler(account, code, subject);
        strategy.saveVerificationCodeToRedis(verifyCodeDTO, code);
    }
}
