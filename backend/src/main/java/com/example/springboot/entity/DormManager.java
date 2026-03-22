package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 宿舍管理员
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

@TableName(value = "dorm_manager")
public class DormManager {

    @TableId("username")
    @NotBlank(message = "用户名不能为空")
    private String username;
    @TableField("password")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;
    @TableField("dormbuild_id")
    @NotNull(message = "楼宇ID不能为空")
    private Integer dormBuildId;
    @TableField("name")
    @NotBlank(message = "姓名不能为空")
    private String name;
    @TableField("gender")
    private String gender;
    @TableField("age")
    @Min(value = 18, message = "年龄不能小于18岁")
    private int age;
    @TableField("phone_num")
    @NotBlank(message = "联系方式不能为空")
    private String phoneNum;
    @TableField("email")
    private String email;
    @TableField("avatar")
    private String avatar;
}
