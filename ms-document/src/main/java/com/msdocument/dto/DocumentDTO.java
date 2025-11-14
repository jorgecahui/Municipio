package com.msdocument.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class DocumentDTO {
    private Long id;
    private String documentNumber;

    @NotNull
    private Long documentTypeId;
    private DocumentTypeDTO documentType;

    @NotNull
    private Long citizenId;

    @NotBlank
    @Size(max = 200)
    private String subject;

    private String description;
    private String status;
    private Long currentAreaId;
    private Long assignedEmployeeId;
    private Integer priority;

    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private String rejectionReason;

    private List<DocumentAttachmentDTO> attachments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
