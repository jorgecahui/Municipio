package com.msworkflow.controller;

import com.msworkflow.dto.TaskAssignmentDTO;
import com.msworkflow.dto.TaskCompletionDTO;
import com.msworkflow.dto.WorkflowInstanceDTO;
import com.msworkflow.dto.WorkflowTaskDTO;
import com.msworkflow.service.WorkflowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workflows")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;

    @PostMapping("/start")
    public ResponseEntity<WorkflowInstanceDTO> start(
            @RequestParam Long documentId,
            @RequestParam Long templateId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workflowService.startWorkflow(documentId, templateId));
    }

    @PostMapping("/tasks/assign")
    public ResponseEntity<WorkflowTaskDTO> assignTask(@Valid @RequestBody TaskAssignmentDTO dto) {
        return ResponseEntity.ok(workflowService.assignTask(dto));
    }

    @PostMapping("/tasks/complete")
    public ResponseEntity<WorkflowTaskDTO> completeTask(@Valid @RequestBody TaskCompletionDTO dto) {
        return ResponseEntity.ok(workflowService.completeTask(dto));
    }

    @GetMapping("/document/{documentId}")
    public ResponseEntity<WorkflowInstanceDTO> getByDocument(@PathVariable Long documentId) {
        return ResponseEntity.ok(workflowService.getByDocumentId(documentId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<WorkflowInstanceDTO>> getByStatus(
            @PathVariable String status, Pageable pageable) {
        return ResponseEntity.ok(workflowService.getByStatus(status, pageable));
    }

    @GetMapping("/tasks/employee/{employeeId}")
    public ResponseEntity<Page<WorkflowTaskDTO>> getTasksByEmployee(
            @PathVariable Long employeeId,
            @RequestParam(defaultValue = "PENDING") String status,
            Pageable pageable) {
        return ResponseEntity.ok(workflowService.getTasksByEmployee(employeeId, status, pageable));
    }

    @GetMapping("/tasks/area/{areaId}")
    public ResponseEntity<Page<WorkflowTaskDTO>> getTasksByArea(
            @PathVariable Long areaId,
            @RequestParam(defaultValue = "PENDING") String status,
            Pageable pageable) {
        return ResponseEntity.ok(workflowService.getTasksByArea(areaId, status, pageable));
    }

    @GetMapping("/{instanceId}/tasks")
    public ResponseEntity<List<WorkflowTaskDTO>> getTasksByInstance(@PathVariable Long instanceId) {
        return ResponseEntity.ok(workflowService.getTasksByInstance(instanceId));
    }
}