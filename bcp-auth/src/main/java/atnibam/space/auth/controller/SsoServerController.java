package atnibam.space.auth.controller;

import atnibam.space.auth.service.SsoService;
import atnibam.space.common.core.domain.dto.LoginRequestDTO;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.sign.SaSignUtil;
import cn.dev33.satoken.sso.SaSsoProcessor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * SSO Server端 Controller
 */
@Api("认证模块")
@RestController
@RequestMapping("/api/auth")
public class SsoServerController {
    @Resource
    private SsoService ssoService;

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
    @ApiOperation("单点登陆接口")
    @RequestMapping(method = RequestMethod.POST, value = "/doLogin")
    public SaResult ssoLogin(@Validated @RequestBody LoginRequestDTO loginRequestDTO) throws IOException {
        ssoService.SsoLoginByCodeHandler(loginRequestDTO);
        return SaResult.ok().setData(StpUtil.getTokenValue());
    }

    /**
     * 根据密钥解析的ID获取用户信息接口
     *
     * @param loginId 密钥解析的ID
     * @param appCode 应用ID
     * @return 用户信息
     */
    @ApiOperation("根据密钥解析的ID获取用户信息接口")
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    public SaResult getData(@ApiParam("密钥解析的ID") String loginId, @ApiParam("应用ID") String appCode) {
        // 校验签名：只有拥有正确秘钥发起的请求才能通过校验
        SaSignUtil.checkRequest(SaHolder.getRequest());
        return SaResult.ok().setData(ssoService.queryUserInfoByCredentialsId(loginId, appCode));
    }

    /**
     * 单点注销
     *
     * @return 注销结果
     */
    @ApiOperation("请求头携带密钥进行单点注销接口")
    @RequestMapping(value = "/signout", method = RequestMethod.GET)
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
    @RequestMapping(method = RequestMethod.GET, value = "/isLogin")
    public Object isLogin() {
        return SaResult.data(StpUtil.isLogin());
    }

}
