package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 系统管理员
 */

@Data
@AllArgsConstructor
@NoArgsConstructor

@TableName(value = "admin")
public class Admin {

    @TableId(value = "username")
    @NotBlank(message = "用户名不能为空")
    private String username;
    @TableField(value = "password")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;
    @TableField(value = "name")
    @NotBlank(message = "姓名不能为空")
    private String name;
    @TableField(value = "gender")
    private String gender;
    @TableField(value = "age")
    private int age;
    @TableField(value = "phone_num")
    private String phoneNum;
    @TableField(value = "email")
    private String email;
    @TableField("avatar")
    private String avatar;
}
