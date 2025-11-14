package com.msworkflow.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowInstanceDTO {
    private Long id;

    @NotNull
    private Long documentId;

    private Long templateId;
    private Integer currentStep;
    private String status;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    private List<WorkflowTaskDTO> tasks;
}