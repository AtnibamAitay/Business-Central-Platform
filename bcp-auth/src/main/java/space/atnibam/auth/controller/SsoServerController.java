package space.atnibam.auth.controller;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.sign.SaSignUtil;
import cn.dev33.satoken.sso.SaSsoProcessor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.atnibam.api.auth.dto.LoginDTO;
import space.atnibam.api.auth.dto.LoginRequestDTO;
import space.atnibam.auth.model.dto.AccountVerificationDTO;
import space.atnibam.auth.service.SsoService;
import space.atnibam.common.core.domain.R;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * SSO Server端 Controller
 */
@Api(value = "认证模块", tags = "认证模块")
@RestController
@RequestMapping("/api/auth")
public class SsoServerController {
    @Resource
    private SsoService ssoService;

    /**
     * 发送验证码
     *
     * @param accountVerificationDTO 包含账号、验证码类型等信息的数据传输对象
     * @return Result 发送结果，成功发送则返回成功信息，否则返回失败原因
     */
    @ApiOperation(value = "发送验证码")
    @PostMapping("/verification-codes")
    public R sendCodeByAccount(@RequestBody @Validated AccountVerificationDTO accountVerificationDTO) {
        return ssoService.sendCode(accountVerificationDTO);
    }

    /**
     * 访问sso请求的默认接口
     *
     * @return sso请求的默认接口
     */
    @ApiIgnore
    @RequestMapping("/*")
    public Object ssoRequest() {
        return SaSsoProcessor.instance.serverDister();
    }

    /**
     * 单点登陆接口
     *
     * @param loginRequestDTO 登录请求DTO
     * @return 认证结果
     */
    @ApiOperation("单点登陆")
    @PostMapping("/login")
    public LoginDTO ssoLogin(@Validated @RequestBody LoginRequestDTO loginRequestDTO) throws IOException {
        return ssoService.ssoLoginByCodeHandler(loginRequestDTO);
    }

    /**
     * 根据密钥解析的ID获取用户信息接口
     *
     * @param loginId 密钥解析的ID
     * @param appId   应用ID
     * @return 用户信息
     */
    @ApiOperation("根据密钥解析的ID获取用户信息")
    @GetMapping("/getData")
    public SaResult getData(@ApiParam("密钥解析的ID") String loginId, @ApiParam("应用ID") String appId) {
        // 校验签名：只有拥有正确秘钥发起的请求才能通过校验
        SaSignUtil.checkRequest(SaHolder.getRequest());
        return SaResult.ok().setData(ssoService.queryUserInfoByCredentialsId(loginId, appId));
    }

    /**
     * 单点注销
     *
     * @return 注销结果
     */
    @ApiOperation("单点注销")
    @GetMapping("/signout")
    public Object ssoSignout() {
        return SaSsoProcessor.instance.ssoSignout();
    }

    /**
     * 根据ticket校验并获取账号ID
     *
     * @return 账号ID
     */
    @ApiIgnore
    @RequestMapping("/checkTicket")
    public Object ssoCheckTicket() {
        return SaSsoProcessor.instance.ssoCheckTicket();
    }

    /**
     * 统一认证地址
     *
     * @return 认证结果
     */
    @ApiIgnore
    @RequestMapping("/auth")
    public Object ssoAuth() {
        return SaSsoProcessor.instance.ssoAuth();
    }

    /**
     * 查询当前是否登录
     *
     * @return 是否登录
     */
    @ApiOperation("查询当前是否登录")
    @GetMapping("/isLogin")
    public Object isLogin() {
        return SaResult.data(StpUtil.isLogin());
    }

}