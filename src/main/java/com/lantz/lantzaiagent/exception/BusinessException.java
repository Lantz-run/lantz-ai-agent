package com.lantz.lantzaiagent.exception;

import lombok.Getter;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/6
 *
 * @author Lantz
 * @version 1.0
 * @Description BussinessException
 * @since 1.8
 */

@Getter
public class BusinessException extends RuntimeException{

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
}
