package com.example.springboot.common;

/**
 * 权限不足异常
 * 用于处理用户权限不够的情况
 */
public class UnauthorizedException extends RuntimeException {

    /**
     * 构造函数
     *
     * @param message 错误消息，如 "权限不足，无法访问"
     */
    public UnauthorizedException(String message) {
        super(message);
    }

    /**
     * 默认构造函数
     * 使用默认错误消息："权限不足，无法访问"
     */
    public UnauthorizedException() {
        this("权限不足，无法访问");
    }
}
