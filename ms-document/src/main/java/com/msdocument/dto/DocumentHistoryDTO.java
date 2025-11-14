package com.msdocument.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentHistoryDTO {
    private Long id;
    private Long documentId;
    private String action;
    private String previousStatus;
    private String newStatus;
    private String comments;
    private Long performedBy;
    private Long fromAreaId;
    private Long toAreaId;
    private LocalDateTime createdAt;
}
