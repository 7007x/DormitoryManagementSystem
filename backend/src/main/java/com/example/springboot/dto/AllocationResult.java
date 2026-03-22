package com.example.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 智能宿舍分配结果DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllocationResult {

    /**
     * 成功分配的记录列表
     */
    private List<AllocationRecord> successList;

    /**
     * 分配失败的学生用户名列表
     */
    private List<String> failList;

    /**
     * 总分配人数
     */
    private Integer totalAllocated;

    /**
     * 分配后房间利用率 (0-1)
     */
    private Double roomUtilization;

    /**
     * 分配耗时（毫秒）
     */
    private Long timeSpent;

    /**
     * 创建成功结果
     */
    public static AllocationResult success(
            List<AllocationRecord> successList,
            List<String> failList,
            Double roomUtilization,
            Long timeSpent) {
        return new AllocationResult(
                successList,
                failList,
                successList.size(),
                roomUtilization,
                timeSpent
        );
    }

    /**
     * 创建失败结果
     */
    public static AllocationResult error(List<String> failList) {
        return new AllocationResult(
                List.of(),
                failList,
                0,
                0.0,
                0L
        );
    }
}
