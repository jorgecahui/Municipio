package com.msauth.repository;

import com.msauth.entity.RefreshToken;
import com.msauth.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUsuario(Usuario usuario);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.fechaExpiracion < :fecha")
    void deleteByFechaExpiracionBefore(LocalDateTime fecha);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revocado = true WHERE rt.usuario = :usuario")
    void revokeAllByUsuario(Usuario usuario);
}
