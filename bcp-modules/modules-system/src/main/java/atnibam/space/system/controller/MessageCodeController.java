package atnibam.space.system.controller;

import atnibam.space.common.core.domain.dto.VerifyCodeDTO;
import atnibam.space.system.service.CodeService;
import atnibam.space.common.core.domain.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: gaojianjie
 * @Description 短信控制层
 * @date 2023/9/12 20:38
 */
@RestController
@RequestMapping("/message-code")
public class MessageCodeController {
    @Autowired
    private CodeService codeService;
    @PostMapping
    public R sendCodeByAccount(@RequestBody @Validated VerifyCodeDTO verifyCodeDTO){
        codeService.sendCode(verifyCodeDTO);
        return R.ok();
    }
}
