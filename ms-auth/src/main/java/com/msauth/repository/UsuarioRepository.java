package com.msauth.repository;

import com.msauth.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByDni(String dni);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.roles r JOIN FETCH r.permisos WHERE u.username = :username")
    Optional<Usuario> findByUsernameWithRolesAndPermissions(String username);
}