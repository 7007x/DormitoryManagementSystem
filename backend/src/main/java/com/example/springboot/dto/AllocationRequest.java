package com.example.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 智能宿舍分配请求DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllocationRequest {

    /**
     * 待分配学生用户名列表
     */
    @NotEmpty(message = "学生列表不能为空")
    private List<String> usernames;
}
