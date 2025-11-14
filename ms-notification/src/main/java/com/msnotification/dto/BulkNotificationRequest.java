package com.msnotification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkNotificationRequest {
    @NotEmpty
    private List<Long> userIds;

    @NotBlank
    private String type;

    @NotBlank
    private String templateCode;

    private Map<String, String> variables;
}
