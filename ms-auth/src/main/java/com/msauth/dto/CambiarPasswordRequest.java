package com.msauth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CambiarPasswordRequest {

    @NotBlank(message = "El password actual es obligatorio")
    private String passwordActual;

    @NotBlank(message = "El nuevo password es obligatorio")
    @Size(min = 8, message = "El password debe tener al menos 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "El password debe contener al menos una mayúscula, una minúscula, un número y un carácter especial"
    )
    private String nuevoPassword;
}
