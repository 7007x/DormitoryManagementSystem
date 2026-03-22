package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 学生
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
// 告诉Mybatis-plus，这个类与数据库中的哪张表有关
@TableName(value = "student")
public class Student {
    // 告诉Mybatis-plus, 属性对应表中的字段

    @TableId(value = "username")
    @NotBlank(message = "用户名不能为空")
    private String username;
    @TableField("password")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;
    @TableField("name")
    @NotBlank(message = "姓名不能为空")
    private String name;
    @TableField("age")
    @Min(value = 15, message = "年龄不能小于15岁")
    @Max(value = 40, message = "年龄不能大于40岁")
    private int age;
    @TableField("gender")
    private String gender;
    @TableField("phone_num")
    private String phoneNum;
    @TableField("email")
    private String email;
    @TableField("avatar")
    private String avatar;
}
