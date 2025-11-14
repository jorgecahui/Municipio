package com.msdocument.controller;

import com.msdocument.dto.DocumentAssignmentDTO;
import com.msdocument.dto.DocumentDTO;
import com.msdocument.dto.DocumentHistoryDTO;
import com.msdocument.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public ResponseEntity<DocumentDTO> create(@Valid @RequestBody DocumentDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentService.create(dto));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<DocumentDTO> submit(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.submit(id));
    }

    @PostMapping("/assign")
    public ResponseEntity<DocumentDTO> assign(@Valid @RequestBody DocumentAssignmentDTO dto) {
        return ResponseEntity.ok(documentService.assign(dto));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<DocumentDTO> approve(
            @PathVariable Long id,
            @RequestParam String comments,
            @RequestParam Long employeeId) {
        return ResponseEntity.ok(documentService.approve(id, comments, employeeId));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<DocumentDTO> reject(
            @PathVariable Long id,
            @RequestParam String reason,
            @RequestParam Long employeeId) {
        return ResponseEntity.ok(documentService.reject(id, reason, employeeId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getById(id));
    }

    @GetMapping("/citizen/{citizenId}")
    public ResponseEntity<Page<DocumentDTO>> getByCitizen(
            @PathVariable Long citizenId, Pageable pageable) {
        return ResponseEntity.ok(documentService.getByCitizen(citizenId, pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<DocumentDTO>> getByStatus(
            @PathVariable String status, Pageable pageable) {
        return ResponseEntity.ok(documentService.getByStatus(status, pageable));
    }

    @GetMapping("/area/{areaId}")
    public ResponseEntity<Page<DocumentDTO>> getByArea(
            @PathVariable Long areaId, Pageable pageable) {
        return ResponseEntity.ok(documentService.getByArea(areaId, pageable));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Page<DocumentDTO>> getByEmployee(
            @PathVariable Long employeeId, Pageable pageable) {
        return ResponseEntity.ok(documentService.getByEmployee(employeeId, pageable));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<DocumentHistoryDTO>> getHistory(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.getHistory(id));
    }
}
