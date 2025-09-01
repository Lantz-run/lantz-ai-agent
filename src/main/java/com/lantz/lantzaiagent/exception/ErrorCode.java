package com.lantz.lantzaiagent.exception;

import lombok.Getter;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/6
 *
 * @author Lantz
 * @version 1.0
 * @Description ErrorCode
 * @since 1.8
 */
@Getter
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    TEXT_ERROR(42200, "含违禁词"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NOT_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40401, "请求数据不存在"),
    FORBIDDEN_ERROR(40301, "禁止访问"),
    SYSTEM_ERROR(50000, "系统错误"),
    OPERATION_ERROR(50001, "操作错误");

    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
