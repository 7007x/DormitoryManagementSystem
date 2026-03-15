package com.example.springboot.common;

import java.lang.annotation.*;

/**
 * 权限注解
 * 用于标记接口需要的访问角色
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {

    /**
     * 允许访问的角色数组
     * 可选值: "admin"（管理员）, "dormManager"（宿管）, "stu"（学生）
     *
     * 使用数组而非单个字符串的原因：
     * 某些接口允许多种角色访问（如查询学生信息，管理员和宿管都可访问）
     */
    String[] value();
}
