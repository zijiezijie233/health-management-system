package com.health.common;

/**
 * 响应码枚举
 * 
 * @author Health Team
 * @since 2024-01-20
 */
public enum ResultCode {
    
    // 通用响应码
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不允许"),
    CONFLICT(409, "资源冲突"),
    TOO_MANY_REQUESTS(429, "请求过于频繁"),
    
    // 用户相关响应码 (1000-1999)
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    USER_DISABLED(1003, "用户已被禁用"),
    USER_LOGIN_FAILED(1004, "用户登录失败"),
    USER_TOKEN_EXPIRED(1005, "用户token已过期"),
    USER_TOKEN_INVALID(1006, "用户token无效"),
    USER_PERMISSION_DENIED(1007, "用户权限不足"),
    
    // 微信相关响应码 (2000-2999)
    WECHAT_CODE_INVALID(2001, "微信授权码无效"),
    WECHAT_API_ERROR(2002, "微信API调用失败"),
    WECHAT_LOGIN_FAILED(2003, "微信登录失败"),
    
    // 药品相关响应码 (3000-3999)
    DRUG_NOT_FOUND(3001, "药品不存在"),
    DRUG_BARCODE_INVALID(3002, "药品条形码无效"),
    DRUG_API_ERROR(3003, "药品API调用失败"),
    DRUG_SEARCH_FAILED(3004, "药品搜索失败"),
    
    // 用药计划相关响应码 (4000-4999)
    MEDICATION_PLAN_NOT_FOUND(4001, "用药计划不存在"),
    MEDICATION_PLAN_CONFLICT(4002, "用药计划冲突"),
    MEDICATION_PLAN_EXPIRED(4003, "用药计划已过期"),
    MEDICATION_RECORD_NOT_FOUND(4004, "用药记录不存在"),
    
    // 体征数据相关响应码 (5000-5999)
    VITAL_SIGNS_NOT_FOUND(5001, "体征数据不存在"),
    VITAL_SIGNS_INVALID(5002, "体征数据无效"),
    DEVICE_NOT_FOUND(5003, "设备不存在"),
    DEVICE_OFFLINE(5004, "设备离线"),
    
    // 文件上传相关响应码 (6000-6999)
    FILE_UPLOAD_FAILED(6001, "文件上传失败"),
    FILE_TYPE_NOT_SUPPORTED(6002, "文件类型不支持"),
    FILE_SIZE_EXCEEDED(6003, "文件大小超出限制"),
    FILE_NOT_FOUND(6004, "文件不存在"),
    
    // 系统相关响应码 (9000-9999)
    SYSTEM_ERROR(9001, "系统错误"),
    DATABASE_ERROR(9002, "数据库错误"),
    NETWORK_ERROR(9003, "网络错误"),
    THIRD_PARTY_API_ERROR(9004, "第三方API错误"),
    CONFIG_ERROR(9005, "配置错误");
    
    private final Integer code;
    private final String message;
    
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    /**
     * 根据响应码获取枚举
     */
    public static ResultCode getByCode(Integer code) {
        for (ResultCode resultCode : values()) {
            if (resultCode.getCode().equals(code)) {
                return resultCode;
            }
        }
        return ERROR;
    }
}