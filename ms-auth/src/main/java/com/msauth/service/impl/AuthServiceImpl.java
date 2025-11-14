package com.msauth.service.impl;

import com.msauth.config.JwtUtil;
import com.msauth.dto.*;
import com.msauth.entity.RefreshToken;
import com.msauth.entity.Rol;
import com.msauth.entity.Usuario;
import com.msauth.repository.RefreshTokenRepository;
import com.msauth.repository.RolRepository;
import com.msauth.repository.UsuarioRepository;
import com.msauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Resetear intentos fallidos
            usuario.setIntentosFallidos(0);
            usuario.setFechaBloqueo(null);
            usuario.setUltimoAcceso(LocalDateTime.now());
            usuarioRepository.save(usuario);

            // Generar tokens
            String accessToken = jwtUtil.generateToken(userDetails);
            String refreshToken = createRefreshToken(usuario);

            UsuarioDto usuarioDto = convertToDto(usuario);

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(jwtUtil.getExpirationTime())
                    .usuario(usuarioDto)
                    .build();

        } catch (BadCredentialsException e) {
            handleFailedLogin(request.getUsername());
            throw new RuntimeException("Credenciales inválidas");
        }
    }

    @Override
    @Transactional
    public ApiResponse<UsuarioDto> registro(RegistroRequest request) {
        // Validar que no exista el usuario
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            return ApiResponse.error("El username ya está en uso");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return ApiResponse.error("El email ya está en uso");
        }

        if (usuarioRepository.existsByDni(request.getDni())) {
            return ApiResponse.error("El DNI ya está registrado");
        }

        // Crear usuario
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .dni(request.getDni())
                .activo(true)
                .emailVerificado(false)
                .build();

        // Asignar rol por defecto
        Rol rolCiudadano = rolRepository.findByNombre("CIUDADANO")
                .orElseThrow(() -> new RuntimeException("Rol CIUDADANO no encontrado"));
        usuario.getRoles().add(rolCiudadano);

        Usuario savedUsuario = usuarioRepository.save(usuario);
        UsuarioDto usuarioDto = convertToDto(savedUsuario);

        log.info("Usuario registrado exitosamente: {}", savedUsuario.getUsername());
        return ApiResponse.success("Usuario registrado exitosamente", usuarioDto);
    }

    @Override
    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token no válido"));

        if (refreshToken.getRevocado()) {
            throw new RuntimeException("Refresh token revocado");
        }

        if (refreshToken.isExpirado()) {
            throw new RuntimeException("Refresh token expirado");
        }

        Usuario usuario = refreshToken.getUsuario();
        UserDetails userDetails = usuario;

        String newAccessToken = jwtUtil.generateToken(userDetails);
        String newRefreshToken = createRefreshToken(usuario);

        // Revocar el refresh token anterior
        refreshToken.setRevocado(true);
        refreshTokenRepository.save(refreshToken);

        UsuarioDto usuarioDto = convertToDto(usuario);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(jwtUtil.getExpirationTime())
                .usuario(usuarioDto)
                .build();
    }

    @Override
    @Transactional
    public ApiResponse<Void> logout(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        refreshTokenRepository.revokeAllByUsuario(usuario);

        log.info("Usuario {} cerró sesión", username);
        return ApiResponse.success("Sesión cerrada exitosamente", null);
    }

    @Override
    @Transactional
    public ApiResponse<Void> cambiarPassword(String username, CambiarPasswordRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPasswordActual(), usuario.getPassword())) {
            return ApiResponse.error("Password actual incorrecto");
        }

        usuario.setPassword(passwordEncoder.encode(request.getNuevoPassword()));
        usuarioRepository.save(usuario);

        // Revocar todos los refresh tokens
        refreshTokenRepository.revokeAllByUsuario(usuario);

        log.info("Usuario {} cambió su password", username);
        return ApiResponse.success("Password cambiado exitosamente", null);
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<UsuarioDto> obtenerPerfil(String username) {
        Usuario usuario = usuarioRepository.findByUsernameWithRolesAndPermissions(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        UsuarioDto usuarioDto = convertToDto(usuario);
        return ApiResponse.success("Perfil obtenido exitosamente", usuarioDto);
    }

    private void handleFailedLogin(String username) {
        usuarioRepository.findByUsername(username).ifPresent(usuario -> {
            int intentos = usuario.getIntentosFallidos() + 1;
            usuario.setIntentosFallidos(intentos);

            if (intentos >= 5) {
                usuario.setFechaBloqueo(LocalDateTime.now());
                log.warn("Usuario {} bloqueado por múltiples intentos fallidos", username);
            }

            usuarioRepository.save(usuario);
        });
    }

    private String createRefreshToken(Usuario usuario) {
        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .usuario(usuario)
                .fechaExpiracion(LocalDateTime.now().plusDays(30))
                .build();

        refreshTokenRepository.save(refreshToken);
        return token;
    }

    private UsuarioDto convertToDto(Usuario usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .dni(usuario.getDni())
                .activo(usuario.getActivo())
                .emailVerificado(usuario.getEmailVerificado())
                .ultimoAcceso(usuario.getUltimoAcceso())
                .roles(usuario.getRoles().stream()
                        .map(Rol::getNombre)
                        .collect(Collectors.toSet()))
                .permisos(usuario.getAuthorities().stream()
                        .map(auth -> auth.getAuthority())
                        .collect(Collectors.toSet()))
                .build();
    }
}
