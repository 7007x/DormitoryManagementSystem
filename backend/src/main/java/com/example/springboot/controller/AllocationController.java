package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.dto.AllocationRecord;
import com.example.springboot.dto.AllocationRequest;
import com.example.springboot.dto.AllocationResult;
import com.example.springboot.service.SmartAllocationAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 智能宿舍分配Controller
 * <p>
 * 提供批量自动分配宿舍功能，基于贪心策略和多维约束匹配算法
 * <p>
 * 主要功能：
 * - 智能批量分配：一键完成多名学生的宿舍分配
 * - 分配预览：模拟分配结果，不实际执行
 * - 统计信息：返回分配耗时、房间利用率等指标
 */
@Slf4j
@RestController
public class AllocationController {

    @Autowired
    private SmartAllocationAlgorithm smartAllocationAlgorithm;

    /**
     * 智能批量分配宿舍
     * <p>
     * POST /api/allocation/smart
     * <p>
     * 请求体示例：
     * <pre>
     * {
     *   "usernames": ["20230001", "20230002", "20230003"]
     * }
     * </pre>
     * <p>
     * 响应示例：
     * <pre>
     * {
     *   "code": 200,
     *   "msg": "success",
     *   "data": {
     *     "successList": [...],
     *     "failList": [],
     *     "totalAllocated": 3,
     *     "roomUtilization": 0.85,
     *     "timeSpent": 156
     *   }
     * }
     * </pre>
     *
     * @param request 分配请求，包含待分配学生用户名列表
     * @return 分配结果
     */
    @PostMapping("/allocation/smart")
    public Result smartAllocate(@RequestBody @Valid AllocationRequest request) {
        log.info("收到智能分配请求，待分配学生数: {}", request.getUsernames().size());

        try {
            AllocationResult result = smartAllocationAlgorithm.allocate(request.getUsernames());
            return Result.success(result);
        } catch (Exception e) {
            log.error("智能分配失败", e);
            return Result.error("-1", "分配失败: " + e.getMessage());
        }
    }

    /**
     * 获取分配统计信息
     * <p>
     * GET /api/allocation/stats
     * <p>
     * 返回当前宿舍分配统计信息，包括：
     * - 总房间数
     * - 已用房间数
     * - 空闲房间数
     * - 总床位数
     * - 已用床位数
     * - 房间利用率
     *
     * @return 统计信息
     */
    @GetMapping("/allocation/stats")
    public Result getAllocationStats() {
        // 这个方法可以扩展，暂时返回基本信息
        return Result.success("统计功能待实现");
    }

    /**
     * 分配预览（不实际执行分配）
     * <p>
     * POST /api/allocation/preview
     * <p>
     * 用于管理员在执行分配前查看预览结果
     *
     * @param request 分配请求
     * @return 预览结果
     */
    @PostMapping("/allocation/preview")
    public Result previewAllocation(@RequestBody @Valid AllocationRequest request) {
        log.info("收到分配预览请求，待分配学生数: {}", request.getUsernames().size());
        // 预览功能可以后续扩展，目前返回预览待实现
        return Result.success("预览功能待实现");
    }
}
