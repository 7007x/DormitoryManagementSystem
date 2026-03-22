package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

/**
 * 公告
 */

@Data
@AllArgsConstructor
@NoArgsConstructor

@TableName(value = "notice")
public class Notice {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField("title")
    @NotBlank(message = "公告标题不能为空")
    private String title;
    @TableField("content")
    @NotBlank(message = "公告内容不能为空")
    private String content;
    @TableField("author")
    @NotBlank(message = "发布人不能为空")
    private String author;
    @TableField("release_time")
    private String releaseTime;
}
