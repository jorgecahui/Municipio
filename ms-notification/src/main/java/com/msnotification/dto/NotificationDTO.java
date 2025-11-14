package com.msnotification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;

    @NotNull
    private Long userId;

    @NotBlank
    private String type;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;

    private String recipient;
    private String status;
    private String priority;
    private Long relatedEntityId;
    private String relatedEntityType;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
}
