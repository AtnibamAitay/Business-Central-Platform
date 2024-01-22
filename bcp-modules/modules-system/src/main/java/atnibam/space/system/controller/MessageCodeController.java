package atnibam.space.system.controller;

import atnibam.space.common.core.domain.R;
import atnibam.space.common.core.domain.dto.VerifyCodeDTO;
import atnibam.space.system.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信控制层
 */
@RestController
@RequestMapping("/api/auth")
public class MessageCodeController {
    @Autowired
    private CodeService codeService;

    /**
     * 发送验证码
     *
     * @param verifyCodeDTO 验证码验证DTO对象
     * @return 返回结果对象
     */
    @PostMapping("/verification-codes")
    public R sendCodeByAccount(@RequestBody @Validated VerifyCodeDTO verifyCodeDTO) {
        codeService.sendCode(verifyCodeDTO);
        return R.ok();
    }
}
