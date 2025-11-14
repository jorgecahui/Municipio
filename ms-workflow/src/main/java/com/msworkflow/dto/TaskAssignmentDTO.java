package com.msworkflow.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignmentDTO {
    @NotNull
    private Long taskId;

    @NotNull
    private Long employeeId;
}
