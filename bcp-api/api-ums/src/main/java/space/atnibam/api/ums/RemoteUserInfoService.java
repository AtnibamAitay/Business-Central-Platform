package space.atnibam.api.ums;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.atnibam.common.core.domain.R;
import space.atnibam.common.core.domain.UserInfo;

import java.io.IOException;
import java.util.List;

/**
 * 用户信息远程调用接口
 */
@FeignClient(value = "modules-ums", contextId = "userInfo", url = "http://local.atnibam.space:9030")
public interface RemoteUserInfoService {

    /**
     * 查询用户信息
     *
     * @param userId 用户ID
     * @return 用户信息实体类
     */
    @GetMapping("/api/userInfo/full/{userId}")
    R getUserInfo(@PathVariable(value = "userId") String userId);

    /**
     * 注销用户
     *
     * @param userId 用户ID
     * @return 注销结果
     */
    @DeleteMapping("/api/userInfo/{userId}")
    R deleteAccount(@PathVariable(value = "userId") String userId);

    /**
     * 用户注册
     *
     * @param userInfo 注册信息
     * @return 注册结果
     * @throws IOException
     */
    @PostMapping("/api/userInfo")
    R<String> registration(@Validated @RequestBody UserInfo userInfo) throws IOException;

    /**
     * 更新用户信息
     *
     * @param userInfo 更新信息
     * @return 更新结果
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/api/userInfo")
    R updateUserInfo(@Validated @RequestBody UserInfo userInfo);

    /**
     * 检查手机号是否存在
     *
     * @param phoneNumber 手机号
     * @return 手机号是否存在
     */
    @RequestMapping(method = RequestMethod.GET, value = "/api/userInfo/phone-numbers/{phoneNumber}/exists")
    R<Boolean> checkPhoneNumbExit(@PathVariable(value = "phoneNumber") String phoneNumber);

    /**
     * 根据邮箱获取用户信息
     *
     * @param email   邮箱
     * @param appCode 应用编码
     * @return 用户信息
     */
    @RequestMapping(method = RequestMethod.GET, value = "/api/userInfo/email/{email}/appcode/{appCode}")
    R<UserInfo> getUserInfoByEmail(@PathVariable(value = "email") String email, @PathVariable(value = "appCode") String appCode);

    /**
     * 根据手机号获取用户信息
     *
     * @param phone   手机号
     * @param appCode 应用编码
     * @return 用户信息
     */
    @RequestMapping(method = RequestMethod.GET, value = "/api/userInfo/phone/{phone}/appcode/{appCode}")
    R<UserInfo> getUserInfoByPhone(@PathVariable(value = "phone") String phone, @PathVariable(value = "appCode") String appCode);

    /**
     * 根据凭证ID获取用户信息
     *
     * @param appCode       应用编码
     * @param credentialsId 凭证ID
     * @return 用户信息
     */
    @RequestMapping(method = RequestMethod.GET, value = ("/api/userInfo/credentialsId/{credentialsId}/appcode/{appCode}"))
    R<UserInfo> getUserInfoByCredentialsId(@PathVariable(value = "appCode") String appCode, @PathVariable(value = "credentialsId") String credentialsId);

    /**
     * 根据用户id查出用户名、用户头像、用户简介
     *
     * @param userId 用户ID
     * @return 用户信息DTO
     */
    @GetMapping("/api/userInfo/{userId}")
    R getDetailedUserInfo(@PathVariable(value = "userId") Integer userId);

    /**
     * 根据一个或多个用户id查出用户名、用户头像列表
     *
     * @param userIds 用户ID列表
     * @return 用户信息DTO列表
     */
    @PostMapping("/api/userInfo/basic-info")
    R getBasicUserInfo(@RequestBody List<Integer> userIds);
}
