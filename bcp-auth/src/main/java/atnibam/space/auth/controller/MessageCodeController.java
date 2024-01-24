package atnibam.space.auth.controller;

import atnibam.space.auth.model.dto.AccountVerificationDTO;
import atnibam.space.auth.service.CodeService;
import atnibam.space.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码控制层
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class MessageCodeController {
    @Autowired
    private CodeService codeService;

    /**
     * 发送验证码
     *
     * @param accountVerificationDTO 包含账号、验证码类型等信息的数据传输对象
     * @return Result 发送结果，成功发送则返回成功信息，否则返回失败原因
     */
    @ApiOperation(value = "发送验证码")
    @PostMapping("/verification-codes")
    public R sendCodeByAccount(@RequestBody @Validated AccountVerificationDTO accountVerificationDTO) {
        log.info("发送验证码接口的入参为：" + accountVerificationDTO);
        return codeService.sendCode(accountVerificationDTO);
    }
}
