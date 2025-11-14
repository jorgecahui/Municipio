package com.msauth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "El password es obligatorio")
    private String password;
}
