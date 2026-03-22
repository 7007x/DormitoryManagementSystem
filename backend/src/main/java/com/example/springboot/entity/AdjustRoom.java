package com.example.springboot.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdjustRoom {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField(value = "username")
    @NotBlank(message = "申请人不能为空")
    private String username;
    @TableField(value = "name")
    @NotBlank(message = "申请人姓名不能为空")
    private String name;
    @TableField(value = "currentroom_id")
    @NotNull(message = "当前房间ID不能为空")
    private Integer currentRoomId;
    @TableField(value = "currentbed_id")
    @NotNull(message = "当前床位ID不能为空")
    private Integer currentBedId;
    @TableField(value = "towardsroom_id")
    @NotNull(message = "目标房间ID不能为空")
    private Integer towardsRoomId;
    @TableField(value = "towardsbed_id")
    @NotNull(message = "目标床位ID不能为空")
    private Integer towardsBedId;
    @TableField("state")
    private String state;
    @TableField("apply_time")
    private String applyTime;
    @TableField("finish_time")
    private String finishTime;
}
