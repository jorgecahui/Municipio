package com.msauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDto {

    private Long id;
    private String username;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String dni;
    private Boolean activo;
    private Boolean emailVerificado;
    private LocalDateTime ultimoAcceso;
    private Set<String> roles;
    private Set<String> permisos;
}
