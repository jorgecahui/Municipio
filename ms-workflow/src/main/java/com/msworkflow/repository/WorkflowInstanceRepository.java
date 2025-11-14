package com.msworkflow.repository;

import com.msworkflow.entity.WorkflowInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, Long> {
    Optional<WorkflowInstance> findByDocumentId(Long documentId);
    Page<WorkflowInstance> findByStatus(String status, Pageable pageable);

    @Query("SELECT wi FROM WorkflowInstance wi JOIN wi.tasks t WHERE t.areaId = :areaId AND t.status = 'PENDING'")
    Page<WorkflowInstance> findPendingByArea(Long areaId, Pageable pageable);

    @Query("SELECT COUNT(wi) FROM WorkflowInstance wi WHERE wi.status = :status")
    Long countByStatus(String status);
}
