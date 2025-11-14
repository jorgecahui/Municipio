package com.msauth.controller;

import com.msauth.dto.*;
import com.msauth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<UsuarioDto>> registro(
            @Valid @RequestBody RegistroRequest request) {
        ApiResponse<UsuarioDto> response = authService.registro(request);

        if (response.getSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        LoginResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        String username = getCurrentUsername();
        ApiResponse<Void> response = authService.logout(username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/perfil")
    public ResponseEntity<ApiResponse<UsuarioDto>> obtenerPerfil() {
        String username = getCurrentUsername();
        ApiResponse<UsuarioDto> response = authService.obtenerPerfil(username);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cambiar-password")
    public ResponseEntity<ApiResponse<Void>> cambiarPassword(
            @Valid @RequestBody CambiarPasswordRequest request) {
        String username = getCurrentUsername();
        ApiResponse<Void> response = authService.cambiarPassword(username, request);

        if (response.getSuccess()) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/validar-token")
    public ResponseEntity<ApiResponse<Boolean>> validarToken() {
        return ResponseEntity.ok(ApiResponse.success("Token válido", true));
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
