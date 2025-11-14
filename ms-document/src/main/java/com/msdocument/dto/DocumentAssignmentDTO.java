package com.msdocument.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentAssignmentDTO {
    @NotNull
    private Long documentId;

    @NotNull
    private Long areaId;

    private Long employeeId;

    private String comments;
}
