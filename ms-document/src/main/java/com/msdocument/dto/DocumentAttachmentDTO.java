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
public class DocumentAttachmentDTO {
    private Long id;
    private Long documentId;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String mimeType;
    private LocalDateTime uploadedAt;
    private Long uploadedBy;
}
