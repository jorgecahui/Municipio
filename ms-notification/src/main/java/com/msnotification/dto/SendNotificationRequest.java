package com.msnotification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequest {
    @NotNull
    private Long userId;

    @NotBlank
    private String type; // EMAIL, SMS, IN_APP

    @NotBlank
    private String templateCode;

    private Map<String, String> variables;

    private String priority;
    private Long relatedEntityId;
    private String relatedEntityType;
}
