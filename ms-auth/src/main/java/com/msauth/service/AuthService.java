package com.msauth.service;

import com.msauth.dto.*;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    ApiResponse<UsuarioDto> registro(RegistroRequest request);
    LoginResponse refreshToken(RefreshTokenRequest request);
    ApiResponse<Void> logout(String username);
    ApiResponse<Void> cambiarPassword(String username, CambiarPasswordRequest request);
    ApiResponse<UsuarioDto> obtenerPerfil(String username);
}
