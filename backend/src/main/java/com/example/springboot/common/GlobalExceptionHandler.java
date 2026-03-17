package com.example.springboot.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 统一处理系统中的各类异常
 *
 * @RestControllerAdvice 声明这是全局异常处理器
 * @ExceptionHandler 标注在方法上，指定该方法处理哪种类型的异常
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     *
     * @param e 捕获到的业务异常对象
     * @return 统一的 Result 对象，包含错误码和错误消息
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理权限不足异常
     *
     * @param e 捕获到的权限异常对象
     * @return 统一的 Result 对象，错误码固定为 "403"
     */
    @ExceptionHandler(UnauthorizedException.class)
    public Result<?> handleUnauthorizedException(UnauthorizedException e) {
        log.warn("权限不足: {}", e.getMessage());
        return Result.error("403", e.getMessage());
    }

    /**
     * 处理参数校验异常
     *
     * @param e 捕获到的非法参数异常
     * @return 统一的 Result 对象，错误码固定为 "400"
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("参数错误: {}", e.getMessage());
        return Result.error("400", "参数错误: " + e.getMessage());
    }

    /**
     * 处理所有其他未捕获的异常（兜底处理）
     *
     * @param e 捕获到的异常对象
     * @return 统一的 Result 对象
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error("500", "系统繁忙，请稍后重试");
    }
}
