package com.msworkflow.repository;

import com.msworkflow.entity.WorkflowStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, Long> {
    List<WorkflowStep> findByTemplateIdOrderByStepOrder(Long templateId);
}
