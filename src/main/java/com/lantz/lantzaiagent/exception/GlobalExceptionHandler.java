package com.lantz.lantzaiagent.exception;

import com.lantz.lantzaiagent.common.BaseResponse;
import com.lantz.lantzaiagent.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/8/6
 *
 * @author Lantz
 * @version 1.0
 * @Description GlobalExceptionHandler
 * @since 1.8
 */

/**
 * 防止服务器的报错信息返回给前端，增加风险
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}
