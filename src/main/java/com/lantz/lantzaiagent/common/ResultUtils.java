package com.lantz.lantzaiagent.common;

import com.lantz.lantzaiagent.exception.ErrorCode;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/6
 *
 * @author Lantz
 * @version 1.0
 * @Description ResultUtils
 * @since 1.8
 */
public class ResultUtils {


    /**
     * 成功
     * @param data 数据
     * @param <T> 数据类型
     * @return 响应
     */
    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     * @param code 错误码
     * @param message 错误信息
     * @return 响应
     */
    public static BaseResponse<?> error(int code, String message){
        return new BaseResponse<>(code, null, message);
    }


    /**
     * 失败
     * @param errorCode 错误码
     * @param message 错误信息
     * @return 响应
     */
    public static BaseResponse<?> error(ErrorCode errorCode, String message){
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }


    /**
     * 失败
     * @param errorCode 错误码
     * @return 响应
     */
    public static BaseResponse<?> error(ErrorCode errorCode){
        return new BaseResponse<>(errorCode);
    }
}
