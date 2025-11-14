package com.msuser.repository;

import com.msuser.entity.Citizen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CitizenRepository extends JpaRepository<Citizen, Long> {
    Optional<Citizen> findByAuthUserId(Long authUserId);
    Optional<Citizen> findByDocumentNumber(String documentNumber);
    boolean existsByDocumentNumber(String documentNumber);
    boolean existsByEmail(String email);
    Page<Citizen> findByActiveTrue(Pageable pageable);
    Page<Citizen> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName, Pageable pageable);
}