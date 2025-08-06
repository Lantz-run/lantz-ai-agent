package com.lantz.lantzaiagent.common;

import com.lantz.lantzaiagent.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/6
 *
 * @author Lantz
 * @version 1.0
 * @Description BaseResponse
 * @since 1.8
 */

@Data
public class BaseResponse<T> implements Serializable {

    private int code; // 错误码

    private T data; // 数据

    private String message; // 错误信息

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
