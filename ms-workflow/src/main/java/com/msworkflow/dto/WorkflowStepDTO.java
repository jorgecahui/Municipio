package com.msworkflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowStepDTO {
    private Long id;

    @NotNull
    private Integer stepOrder;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Long areaId;

    @NotNull
    private Integer estimatedDays;

    private Boolean requiresApproval;
    private Boolean isOptional;
}
