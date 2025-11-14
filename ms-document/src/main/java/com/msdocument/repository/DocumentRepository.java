package com.msdocument.repository;

import com.msdocument.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByDocumentNumber(String documentNumber);
    Page<Document> findByCitizenId(Long citizenId, Pageable pageable);
    Page<Document> findByStatus(String status, Pageable pageable);
    Page<Document> findByCurrentAreaId(Long areaId, Pageable pageable);
    Page<Document> findByAssignedEmployeeId(Long employeeId, Pageable pageable);

    @Query("SELECT d FROM Document d WHERE d.status = :status AND d.currentAreaId = :areaId")
    Page<Document> findByStatusAndAreaId(String status, Long areaId, Pageable pageable);

    @Query("SELECT COUNT(d) FROM Document d WHERE d.status = :status")
    Long countByStatus(String status);

    @Query("SELECT COUNT(d) FROM Document d WHERE d.citizenId = :citizenId AND d.status != 'ARCHIVED'")
    Long countActiveByCitizen(Long citizenId);

    List<Document> findBySubmittedAtBetween(LocalDateTime start, LocalDateTime end);
}