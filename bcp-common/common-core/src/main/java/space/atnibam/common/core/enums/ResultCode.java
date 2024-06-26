package space.atnibam.common.core.enums;

import org.springframework.http.HttpStatus;

/**
 * 状态码枚举，用于http响应中的状态码
 */
public enum ResultCode {
    /*成功状态码*/
    SUCCESS(200, "成功", HttpStatus.OK),

    /*参数错误：1001-1999*/
    PARAM_IS_INVALID(1001, "参数无效", HttpStatus.BAD_REQUEST),
    PARAM_IS_BLANK(1002, "参数为空", HttpStatus.BAD_REQUEST),
    PARAM_TYPE_BIND_ERROR(1003, "参数类型错误", HttpStatus.BAD_REQUEST),
    PARAM_NOT_COMPLETE(1004, "参数缺失", HttpStatus.BAD_REQUEST),
    PASSWORD_CONFIRM_ERROR(1005, "两次密码输入不匹配", HttpStatus.BAD_REQUEST),

    /*用户错误：2001-2999*/

    USER_NOT_LOGGED_IN(2001, "用户未登录，访问的路径需要验证，请登录", HttpStatus.UNAUTHORIZED),
    USER_LOGIN_ERROR(2002, "账号不存在或密码错误", HttpStatus.UNAUTHORIZED),
    USER_ACCOUNT_FORBIDDEN(2003, "账号已被禁用", HttpStatus.UNAUTHORIZED),
    USER_HAS_EXISTED(2005, "用户已存在", HttpStatus.CONFLICT),
    USER_VERIFY_ERROR(2008, "验证码校验失败，请重新获取", HttpStatus.UNAUTHORIZED),
    USER_INSERT_ERROR(2010, "用户数据插入异常", HttpStatus.CONFLICT),
    USER_NOT_EXIST_BY_CODE(2012, "账号不存在,请注册", HttpStatus.NOT_FOUND),
    USER_REGISTRATION_TIME_OUT(2014, "用户注册超时，请重新注册", HttpStatus.UNPROCESSABLE_ENTITY),
    USER_ACCOUNT_INCONSISTENT(2015, "注册账号与发送验证码账号不一致", HttpStatus.BAD_REQUEST),
    USER_UPDATE_ERROR(2010, "用户数据更新异常", HttpStatus.CONFLICT),
    USER_LOGOUT_FAIL(2017, "用户注销失败", HttpStatus.CONFLICT),
    LOGIN_METHOD_INVALID(2018, "登录方法无效", HttpStatus.BAD_REQUEST),
    HTTP_METHOD_ERROR(3001, "Http请求方法错误", HttpStatus.BAD_REQUEST),
    PHONE_CODE_OOT(3002, "当天手机号发送验证码次数以达到上限，请24小时后重试", HttpStatus.TOO_MANY_REQUESTS),
    SMS_INTERRUPTED_OR_EXECUTION_ERROR(4001, "阿里云服务被中断或执行错误", HttpStatus.BAD_REQUEST),
    ACCOUNT_FREEZE(4002, "账号暂时冻结", HttpStatus.FORBIDDEN),
    ACCOUNT_LOGOUT(4003, "账号已注销,请重新注册", HttpStatus.FORBIDDEN),
    PHONE_NUM_REGISTERED(4004, "手机号已注销", HttpStatus.OK),
    REQUEST_ID_NULL(4005, "未能读取到有效 requestId", HttpStatus.BAD_REQUEST),
    PHONE_NUM_NON_COMPLIANCE(4006, "手机号不符合规范", HttpStatus.BAD_REQUEST),
    EMAIL_NUM_NON_COMPLIANCE(4007, "邮箱号不符合规范", HttpStatus.BAD_REQUEST),
    ACCOUNT_EXIST(4008, "账号已存在", HttpStatus.OK),
    USERINFO_NON_EXIST(4008, "账号信息不存在", HttpStatus.OK),
    EMAIL_NOT_EXIST(4009, "邮箱不存在", HttpStatus.OK),
    USER_AVATAR_UPLOAD_FAILED(4010, "上传头像失败", HttpStatus.INTERNAL_SERVER_ERROR),

    //重试请求已被处理
    IDEMPOTENCY_ERROR(4006, "请求已处理", HttpStatus.OK),

    /*服务器内部错误：5001-5500*/
    INTERNAL_ERROR(5001, "服务器内部错误，请联系开发人员", HttpStatus.INTERNAL_SERVER_ERROR),
    AUTO_REGISTER_FILE(5002, "自动注册失败,请稍后重试", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVER_BUSY(5003, "服务器繁忙，请稍后重试", HttpStatus.TOO_MANY_REQUESTS),
    MESSAGE_SERVICE_ERROR(5004, "短信服务异常，请稍后重试", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVER_LIMIT(5005, "服务器已限流", HttpStatus.TOO_MANY_REQUESTS),

    /*订单相关错误：5501-6000*/
    PRODUCT_OR_PAY_TYPE_NULL(5501, "产品和付款类型不能为空或空", HttpStatus.BAD_REQUEST),
    CREATE_ORDER_CONTRAST_LOCK_FAIL(5502, "创建订单时校验锁失败", HttpStatus.BAD_REQUEST),
    INSERT_ORDER_FAIL(5503, "插入订单失败", HttpStatus.BAD_REQUEST),
    CREATE_ORDER_FAIL(5504, "创建订单失败，请重试", HttpStatus.BAD_REQUEST),
    ORDER_NOT_EXIST(5505, "订单不存在", HttpStatus.NOT_FOUND),
    ORDER_PAYING(5506, "订单支付中", HttpStatus.BAD_REQUEST),
    ORDER_PAID(5507, "订单已支付", HttpStatus.BAD_REQUEST),

    /*App相关错误：6001-6100*/
    APP_NOT_FOUND(6001, "App不存在", HttpStatus.NOT_FOUND),
    APP_IS_DISABLE(6002, "App已经被禁用", HttpStatus.FORBIDDEN),

    /**
     * MinIO错误状态码：3301-3500
     */
    MINIO_UPLOAD_ERROR(3301, "上传过程中出错", HttpStatus.BAD_REQUEST),
    MINIO_SAVE_FILE_INFO_ERROR(3302, "保存文件信息失败", HttpStatus.INTERNAL_SERVER_ERROR),
    MINIO_GET_FILE_ERROR(3303, "获取文件异常", HttpStatus.NOT_FOUND),
    MINIO_GET_FILE_BLOCK_ERROR(3304, "获取文件块异常", HttpStatus.NOT_FOUND),
    MINIO_CREATE_MERGE_TEMP_FILE_ERROR(3305, "创建合并临时文件出错", HttpStatus.INTERNAL_SERVER_ERROR),
    MINIO_MERGE_FILE_ERROR(3306, "合并文件过程中出错", HttpStatus.INTERNAL_SERVER_ERROR),
    MINIO_MERGE_FILE_CHECK_ERROR(3307, "合并文件MD5校验失败", HttpStatus.UNPROCESSABLE_ENTITY),
    MINIO_MERGE_FILE_CHECK_EXCEPTION(3308, "合并文件校验异常", HttpStatus.UNPROCESSABLE_ENTITY),
    MINIO_DELETE_TEMP_FILE_ERROR(3309, "临时分块文件删除错误", HttpStatus.INTERNAL_SERVER_ERROR),
    MINIO_DELETE_TEMP_MERGE_FILE_ERROR(3310, "临时合并文件删除错误", HttpStatus.INTERNAL_SERVER_ERROR),
    MINIO_CREATE_TEMP_FILE_ERROR(3311, "创建临时分块文件出错", HttpStatus.INTERNAL_SERVER_ERROR),
    MINIO_QUERY_FILE_BLOCK_ERROR(3312, "查询文件分块出错", HttpStatus.NOT_FOUND),
    MINIO_MEDIA_FILE_INSERT_ERROR(3313, "媒资文件入库出错", HttpStatus.INTERNAL_SERVER_ERROR),
    MINIO_SAVE_AVI_TO_PROCESS_ERROR(3314, "保存avi视频到待处理表失败", HttpStatus.BAD_REQUEST),
    MINIO_CREATE_TEMP_FILE_BEFORE_PROCESS_ERROR(3315, "处理视频前创建临时文件失败", HttpStatus.BAD_REQUEST),
    MINIO_DOWNLOAD_ORIGINAL_FILE_ERROR(3316, "下载原始文件过程出错", HttpStatus.NOT_FOUND),
    MINIO_PROCESS_VIDEO_ERROR(3317, "处理视频失败", HttpStatus.BAD_REQUEST),
    MINIO_UPLOAD_FILE_ERROR(3318, "上传文件失败", HttpStatus.BAD_REQUEST);


    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 接口调用提示信息
     */
    private final String message;

    /**
     * http状态码
     */
    private final HttpStatus status;

    ResultCode(Integer code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}