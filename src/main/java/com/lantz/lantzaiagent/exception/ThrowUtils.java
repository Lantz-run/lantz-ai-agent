package com.lantz.lantzaiagent.exception;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/6
 *
 * @author Lantz
 * @version 1.0
 * @Description ThrowUtils
 * @since 1.8
 */
public class ThrowUtils {


    /**
     * 条件成立则抛出异常
     * @param condition         条件
     * @param runtimeException 异常
     */
    public static void ThrowIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛出异常
     * @param condition 条件
     * @param errorCode 错误码
     */
    public static void ThrowIf(boolean condition, ErrorCode errorCode) {
        ThrowIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛出异常
     * @param condition 条件
     * @param errorCode 错误码
     * @param message 错误信息
     */
    public static void ThrowIf(boolean condition, ErrorCode errorCode, String message) {
        ThrowIf(condition, new BusinessException(errorCode, message));
    }
}
