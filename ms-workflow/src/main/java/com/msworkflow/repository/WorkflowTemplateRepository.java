package com.msworkflow.repository;

import com.msworkflow.entity.WorkflowTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkflowTemplateRepository extends JpaRepository<WorkflowTemplate, Long> {
    Optional<WorkflowTemplate> findByCode(String code);
    Optional<WorkflowTemplate> findByDocumentTypeId(Long documentTypeId);
    List<WorkflowTemplate> findByActiveTrue();
}
