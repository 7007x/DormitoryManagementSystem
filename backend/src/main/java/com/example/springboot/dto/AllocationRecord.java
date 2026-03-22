package com.example.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分配记录DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllocationRecord {

    /**
     * 学生用户名
     */
    private String username;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 性别
     */
    private String gender;

    /**
     * 分配的房间ID
     */
    private Integer roomId;

    /**
     * 楼宇ID
     */
    private Integer buildId;

    /**
     * 楼层号
     */
    private Integer floorNum;

    /**
     * 床位编号 (1-4)
     */
    private Integer bedNum;
}
