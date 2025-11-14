package com.msauth.dto;

import jakarta.validation.constraints.Email;
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
public class RegistroRequest {

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 4, max = 50, message = "El username debe tener entre 4 y 50 caracteres")
    private String username;

    @NotBlank(message = "El password es obligatorio")
    @Size(min = 8, message = "El password debe tener al menos 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "El password debe contener al menos una mayúscula, una minúscula, un número y un carácter especial"
    )
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no debe exceder 100 caracteres")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe tener 8 dígitos")
    private String dni;
}
