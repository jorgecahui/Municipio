package com.msauth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String nombre;

    @Column(length = 200)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rol_permiso",
            joinColumns = @JoinColumn(name = "rol_id"),
            inverseJoinColumns = @JoinColumn(name = "permiso_id")
    )
    private Set<Permiso> permisos = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}
