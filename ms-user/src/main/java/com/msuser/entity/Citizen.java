package com.msuser.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "citizens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Citizen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long authUserId; // Reference to ms-auth User

    @Column(nullable = false, unique = true, length = 20)
    private String documentNumber;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String phoneNumber;

    private LocalDate birthDate;

    @Column(length = 10)
    private String gender; // M, F, OTHER

    @Column(length = 200)
    private String address;

    @Column(length = 100)
    private String district;

    @Column(length = 100)
    private String province;

    @Column(length = 100)
    private String department;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
