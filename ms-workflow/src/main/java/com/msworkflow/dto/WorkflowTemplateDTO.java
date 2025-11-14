package com.msworkflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTemplateDTO {
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private String description;

    @NotNull
    private Long documentTypeId;

    private Boolean active;
    private List<WorkflowStepDTO> steps;
}
