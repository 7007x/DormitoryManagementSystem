package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 宿舍房间
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

@TableName(value = "dorm_room")
public class DormRoom {

    @TableId(value = "dormroom_id")
    private Integer dormRoomId;
    @TableField("dormbuild_id")
    @NotNull(message = "楼宇ID不能为空")
    private Integer dormBuildId;
    @TableField("floor_num")
    @NotNull(message = "楼层不能为空")
    @Min(value = 1, message = "楼层必须大于0")
    private Integer floorNum;
    @TableField("max_capacity")
    @Min(value = 1, message = "容量必须大于0")
    private Integer maxCapacity;
    @TableField("current_capacity")
    private Integer currentCapacity;
    @TableField("first_bed")
    private String firstBed;
    @TableField("second_bed")
    private String secondBed;
    @TableField("third_bed")
    private String thirdBed;
    @TableField("fourth_bed")
    private String fourthBed;

}
