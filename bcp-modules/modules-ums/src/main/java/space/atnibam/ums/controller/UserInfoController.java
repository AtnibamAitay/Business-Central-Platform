package space.atnibam.ums.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.atnibam.api.ums.RemoteUserInfoService;
import space.atnibam.common.core.domain.AuthCredentials;
import space.atnibam.common.core.domain.R;
import space.atnibam.common.core.domain.UserInfo;
import space.atnibam.common.core.enums.ResultCode;
import space.atnibam.common.core.exception.UserOperateException;
import space.atnibam.common.core.utils.StringUtils;
import space.atnibam.ums.model.dto.UpdateAvatarDTO;
import space.atnibam.ums.model.dto.UpdateUserNameDTO;
import space.atnibam.ums.model.dto.UserInfoDTO;
import space.atnibam.ums.service.AuthCredentialsService;
import space.atnibam.ums.service.UserInfoService;

import java.io.IOException;
import java.util.Objects;

import static space.atnibam.common.core.enums.ResultCode.USER_AVATAR_UPLOAD_FAILED;

/**
 * 用户基本信息控制层
 */
@Api(value = "用户信息", tags = "用户信息")
@RestController
@RequestMapping("/api/userInfo")
public class UserInfoController implements RemoteUserInfoService {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private AuthCredentialsService authCredentialsService;

    /**
     * 查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @ApiOperation(value = "查询用户信息")
    @Override
    @GetMapping("/full/{userId}")
    public R<UserInfo> getUserFullInfo(@PathVariable(value = "userId") String userId) {
        return R.ok(userInfoService.queryUserInfo(userId));
    }

    /**
     * 根据用户id查出用户名、用户头像、用户简介
     *
     * @param userId 用户ID
     * @return 用户信息DTO
     */
    @Override
    @ApiOperation(value = "根据用户id查出用户名、用户头像、用户简介", notes = "根据用户ID查询用户信息")
    @GetMapping("/{userId}")
    public R queryUserInfo(@PathVariable(value = "userId") Integer userId) {
        UserInfoDTO userInfoDTO = userInfoService.getUserInfoById(userId);
        return R.ok(userInfoDTO);
    }

    /**
     * 注销账号 - 删除账号
     * 会有15天的保护期
     *
     * @param userId 用户ID
     * @return 注销结果
     */
    @ApiOperation(value = "用户注销 - 删除账号")
    @Override
    @DeleteMapping("/{userId}")
    public R deleteAccount(@PathVariable(value = "userId") String userId) {
        userInfoService.logout(userId);
        return R.ok();
    }

    /**
     * 用户注册
     *
     * @param userInfo 用户信息
     * @return 注册结果
     * @throws IOException 输入输出异常
     */
    @ApiOperation(value = "用户注册", notes = "处理用户注册请求")
    @Override
    @PostMapping
    public R<String> registration(@Validated @RequestBody UserInfo userInfo) throws IOException {
        userInfoService.registration(userInfo);
        return R.ok();
    }

    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     * @return 更新结果
     */
    @ApiOperation(value = "更新用户信息", notes = "根据提供的用户信息进行更新")
    @Override
    @RequestMapping(method = RequestMethod.PUT)
    public R updateUserInfo(@Validated @RequestBody UserInfo userInfo) {
        if (Objects.isNull(userInfo.getUserId())) {
            //todo log
            throw new UserOperateException(ResultCode.PARAM_NOT_COMPLETE);
        }
        if (!StringUtils.hasText(userInfo.getUserName())) {
            //todo log
            throw new UserOperateException(ResultCode.PARAM_NOT_COMPLETE);
        }
        userInfoService.updateUserInfo(userInfo);
        return R.ok();
    }

    /**
     * 检查手机号是否存在
     *
     * @param phoneNumber 手机号
     * @return 手机号是否存在
     */
    @ApiOperation(value = "检查手机号是否存在", notes = "通过手机号查询该手机号是否已存在")
    @Override
    @RequestMapping(method = RequestMethod.GET, value = "/phone-numbers/{phoneNumber}/exists")
    public R<Boolean> checkPhoneNumbExit(@PathVariable(value = "phoneNumber") String phoneNumber) {
        AuthCredentials result = authCredentialsService.checkPhoneNumbExit(phoneNumber);
        boolean bool = result != null;
        return R.ok(bool);
    }

    /**
     * 根据邮箱查询用户信息
     *
     * @param email   邮箱
     * @param appCode 应用编码
     * @return 用户信息
     */
    @ApiOperation(value = "根据邮箱查询用户信息", notes = "通过邮箱和应用编码查询用户信息")
    @Override
    @RequestMapping(method = RequestMethod.GET, value = "/email/{email}/appcode/{appCode}")
    public R<UserInfo> getUserInfoByEmail(@PathVariable(value = "email") String email, @PathVariable(value = "appCode") String appCode) {
        UserInfo userInfo = userInfoService.queryUserInfoByEmail(email, appCode);
        return R.ok(userInfo);
    }

    /**
     * 根据手机号查询用户信息
     *
     * @param phone   手机号
     * @param appCode 应用编码
     * @return 用户信息
     */
    @ApiOperation(value = "根据手机号查询用户信息", notes = "通过手机号和应用编码查询用户信息")
    @Override
    @RequestMapping(method = RequestMethod.GET, value = "/phone/{phone}/appcode/{appCode}")
    public R<UserInfo> getUserInfoByPhone(@PathVariable(value = "phone") String phone, @PathVariable(value = "appCode") String appCode) {
        UserInfo userInfo = userInfoService.queryUserInfoByPhone(phone, appCode);
        return R.ok(userInfo);
    }

    /**
     * 根据凭证ID查询用户信息
     *
     * @param appCode       应用编码
     * @param credentialsId 凭证ID
     * @return 用户信息
     */
    @ApiOperation(value = "根据凭证ID查询用户信息", notes = "通过凭证ID和应用编码查询用户信息")
    @Override
    @RequestMapping(method = RequestMethod.GET, value = "/credentialsId/{credentialsId}/appcode/{appCode}")
    public R<UserInfo> getUserInfoByCredentialsId(@PathVariable(value = "appCode") String appCode, @PathVariable(value = "credentialsId") String credentialsId) {
        UserInfo userInfo = userInfoService.queryUserInfoByCredentialsId(credentialsId, appCode);
        return R.ok(userInfo);
    }

    /**
     * 设置用户的用户名
     *
     * @param updateUserNameDTO 包含账号ID、用户名的传输实体
     * @return 设置用户名的结果
     */
    @ApiOperation(value = "设置用户的用户名", notes = "根据提供的UpdateUserNameDTO对象更新用户用户名")
    @PostMapping("/username")
    public R updateUserName(@RequestBody UpdateUserNameDTO updateUserNameDTO) {
        userInfoService.setUserName(updateUserNameDTO);
        return R.ok();
    }

    /**
     * 设置用户头像
     *
     * @param updateAvatarDTO 包含头像信息的传输实体
     * @return 设置头像的结果
     */
    @ApiOperation(value = "设置用户头像", notes = "根据提供的UpdateAvatarDTO对象更新用户头像")
    @PostMapping("/avatar")
    public R updateUserAvatar(@ModelAttribute UpdateAvatarDTO updateAvatarDTO) {
        if (userInfoService.setAvatar(updateAvatarDTO)) {
            return R.ok();
        }
        return R.fail(USER_AVATAR_UPLOAD_FAILED);
    }
}