package com.msworkflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTaskDTO {
    private Long id;
    private Long instanceId;
    private Long stepId;
    private Integer stepOrder;
    private String name;
    private Long areaId;
    private Long assignedEmployeeId;
    private String status;
    private LocalDateTime assignedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String comments;
    private String resolution;
}
