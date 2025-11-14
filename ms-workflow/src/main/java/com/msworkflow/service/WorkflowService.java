package com.msworkflow.service;

import com.msworkflow.dto.TaskAssignmentDTO;
import com.msworkflow.dto.TaskCompletionDTO;
import com.msworkflow.dto.WorkflowInstanceDTO;
import com.msworkflow.dto.WorkflowTaskDTO;
import com.msworkflow.entity.WorkflowInstance;
import com.msworkflow.entity.WorkflowStep;
import com.msworkflow.entity.WorkflowTask;
import com.msworkflow.entity.WorkflowTemplate;
import com.msworkflow.repository.WorkflowInstanceRepository;
import com.msworkflow.repository.WorkflowStepRepository;
import com.msworkflow.repository.WorkflowTaskRepository;
import com.msworkflow.repository.WorkflowTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final WorkflowInstanceRepository instanceRepository;
    private final WorkflowTemplateRepository templateRepository;
    private final WorkflowStepRepository stepRepository;
    private final WorkflowTaskRepository taskRepository;

    @Transactional
    public WorkflowInstanceDTO startWorkflow(Long documentId, Long templateId) {
        WorkflowTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        List<WorkflowStep> steps = stepRepository.findByTemplateIdOrderByStepOrder(templateId);

        WorkflowInstance instance = WorkflowInstance.builder()
                .documentId(documentId)
                .templateId(templateId)
                .currentStep(0)
                .status("PENDING")
                .startedAt(LocalDateTime.now())
                .build();

        instance = instanceRepository.save(instance);

        // Create tasks for each step
        for (WorkflowStep step : steps) {
            WorkflowTask task = WorkflowTask.builder()
                    .instance(instance)
                    .stepId(step.getId())
                    .stepOrder(step.getStepOrder())
                    .name(step.getName())
                    .areaId(step.getAreaId())
                    .status(step.getStepOrder() == 1 ? "PENDING" : "PENDING")
                    .build();
            taskRepository.save(task);
        }

        return toDTO(instanceRepository.findById(instance.getId()).orElseThrow());
    }

    @Transactional
    public WorkflowTaskDTO assignTask(TaskAssignmentDTO dto) {
        WorkflowTask task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setAssignedEmployeeId(dto.getEmployeeId());
        task.setAssignedAt(LocalDateTime.now());
        task.setStatus("IN_PROGRESS");
        task = taskRepository.save(task);

        return toTaskDTO(task);
    }

    @Transactional
    public WorkflowTaskDTO completeTask(TaskCompletionDTO dto) {
        WorkflowTask task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus("COMPLETED");
        task.setCompletedAt(LocalDateTime.now());
        task.setResolution(dto.getResolution());
        task.setComments(dto.getComments());
        task = taskRepository.save(task);

        // Update instance
        WorkflowInstance instance = task.getInstance();
        instance.setCurrentStep(task.getStepOrder());
        instance.setUpdatedAt(LocalDateTime.now());

        // Check if all tasks are completed
        List<WorkflowTask> allTasks = taskRepository.findByInstanceIdOrderByStepOrder(instance.getId());
        boolean allCompleted = allTasks.stream().allMatch(t -> "COMPLETED".equals(t.getStatus()));

        if (allCompleted) {
            instance.setStatus("COMPLETED");
            instance.setCompletedAt(LocalDateTime.now());
        } else {
            instance.setStatus("IN_PROGRESS");
        }

        instanceRepository.save(instance);

        return toTaskDTO(task);
    }

    @Transactional(readOnly = true)
    public WorkflowInstanceDTO getByDocumentId(Long documentId) {
        return instanceRepository.findByDocumentId(documentId)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Workflow not found"));
    }

    @Transactional(readOnly = true)
    public Page<WorkflowInstanceDTO> getByStatus(String status, Pageable pageable) {
        return instanceRepository.findByStatus(status, pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<WorkflowTaskDTO> getTasksByEmployee(Long employeeId, String status, Pageable pageable) {
        return taskRepository.findByAssignedEmployeeIdAndStatus(employeeId, status, pageable)
                .map(this::toTaskDTO);
    }

    @Transactional(readOnly = true)
    public Page<WorkflowTaskDTO> getTasksByArea(Long areaId, String status, Pageable pageable) {
        return taskRepository.findByAreaIdAndStatus(areaId, status, pageable)
                .map(this::toTaskDTO);
    }

    @Transactional(readOnly = true)
    public List<WorkflowTaskDTO> getTasksByInstance(Long instanceId) {
        return taskRepository.findByInstanceIdOrderByStepOrder(instanceId)
                .stream()
                .map(this::toTaskDTO)
                .collect(Collectors.toList());
    }

    private WorkflowInstanceDTO toDTO(WorkflowInstance instance) {
        List<WorkflowTaskDTO> tasks = taskRepository.findByInstanceIdOrderByStepOrder(instance.getId())
                .stream()
                .map(this::toTaskDTO)
                .collect(Collectors.toList());

        return WorkflowInstanceDTO.builder()
                .id(instance.getId())
                .documentId(instance.getDocumentId())
                .templateId(instance.getTemplateId())
                .currentStep(instance.getCurrentStep())
                .status(instance.getStatus())
                .startedAt(instance.getStartedAt())
                .completedAt(instance.getCompletedAt())
                .tasks(tasks)
                .build();
    }

    private WorkflowTaskDTO toTaskDTO(WorkflowTask task) {
        return WorkflowTaskDTO.builder()
                .id(task.getId())
                .instanceId(task.getInstance().getId())
                .stepId(task.getStepId())
                .stepOrder(task.getStepOrder())
                .name(task.getName())
                .areaId(task.getAreaId())
                .assignedEmployeeId(task.getAssignedEmployeeId())
                .status(task.getStatus())
                .assignedAt(task.getAssignedAt())
                .startedAt(task.getStartedAt())
                .completedAt(task.getCompletedAt())
                .comments(task.getComments())
                .resolution(task.getResolution())
                .build();
    }
}
