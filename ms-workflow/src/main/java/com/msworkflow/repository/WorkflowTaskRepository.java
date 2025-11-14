package com.msworkflow.repository;

import com.msworkflow.entity.WorkflowTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WorkflowTaskRepository extends JpaRepository<WorkflowTask, Long> {
    List<WorkflowTask> findByInstanceIdOrderByStepOrder(Long instanceId);
    Page<WorkflowTask> findByAssignedEmployeeIdAndStatus(Long employeeId, String status, Pageable pageable);
    Page<WorkflowTask> findByAreaIdAndStatus(Long areaId, String status, Pageable pageable);

    @Query("SELECT t FROM WorkflowTask t WHERE t.instance.id = :instanceId AND t.stepOrder = :stepOrder")
    Optional<WorkflowTask> findByInstanceIdAndStepOrder(Long instanceId, Integer stepOrder);
}
