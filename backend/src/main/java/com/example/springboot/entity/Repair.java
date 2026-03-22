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

/**
 * 报修单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

@TableName(value = "repair")
public class Repair {

    @TableId(value = "id")
    private Integer id;
    @TableField("repairer")
    @NotBlank(message = "报修人不能为空")
    private String repairer;
    @TableField("dormbuild_id")
    @NotNull(message = "楼宇ID不能为空")
    private Integer dormBuildId;
    @TableField("dormroom_id")
    @NotNull(message = "房间ID不能为空")
    private Integer dormRoomId;
    @TableField("title")
    @NotBlank(message = "报修标题不能为空")
    private String title;
    @TableField("content")
    @NotBlank(message = "报修内容不能为空")
    private String content;
    @TableField("state")
    private String state;
    @TableField("order_buildtime")
    private String orderBuildTime;
    @TableField("order_finishtime")
    private String orderFinishTime;
}
