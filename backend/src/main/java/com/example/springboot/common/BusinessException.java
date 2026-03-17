package com.example.springboot.common;

/**
 * 业务异常类
 * 用于处理业务逻辑中的异常情况
 */
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     * 例如："400", "404", "1001" 等
     */
    private String code;

    /**
     * 构造函数
     *
     * @param code    错误码，如 "400", "404"
     * @param message 错误消息，如 "用户不存在"
     */
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造函数（默认错误码为500）
     *
     * @param message 错误消息
     */
    public BusinessException(String message) {
        this("500", message);
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置错误码
     *
     * @param code 错误码
     */
    public void setCode(String code) {
        this.code = code;
    }
}
