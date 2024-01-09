package atnibam.space.system.service.impl;

import atnibam.space.common.core.constant.Constants;
import atnibam.space.common.core.enums.CertificateMethodEnum;
import cn.hutool.core.util.RandomUtil;
import atnibam.space.common.core.domain.dto.VerifyCodeDTO;
import atnibam.space.system.factory.CertificateStrategyFactory;
import atnibam.space.system.service.CodeService;
import atnibam.space.system.strategy.CertificateStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: gaojianjie
 * @Description 短信服务
 * @date 2023/9/12 19:24
 */
@Service
public class CodeServiceImpl implements CodeService {
    @Autowired
    private CertificateStrategyFactory certificateStrategyFactory;
    @Override
    public void sendCode(VerifyCodeDTO verifyCodeDTO) {
        //生成验证码
        String code = RandomUtil.randomNumbers(Constants.RANDOM_LENGTH);
        //提取发送目标
        String account = verifyCodeDTO.getAccount();
        //提取主题信息
        String subject = verifyCodeDTO.getSubject();
        //发送验证码
        CertificateStrategy strategy = certificateStrategyFactory.getStrategy(CertificateMethodEnum.fromCode(verifyCodeDTO.getIdentity()));
        strategy.sendCodeHandler(account,code,subject);
        strategy.saveVerificationCodeToRedis(verifyCodeDTO,code);
    }
}
