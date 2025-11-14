package com.msworkflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCompletionDTO {
    @NotNull
    private Long taskId;

    @NotBlank
    private String resolution;

    private String comments;
}
