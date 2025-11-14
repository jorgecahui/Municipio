package com.msdocument.service;

import com.msdocument.dto.DocumentAssignmentDTO;
import com.msdocument.dto.DocumentDTO;
import com.msdocument.dto.DocumentHistoryDTO;
import com.msdocument.entity.Document;
import com.msdocument.entity.DocumentHistory;
import com.msdocument.entity.DocumentType;
import com.msdocument.repository.DocumentAttachmentRepository;
import com.msdocument.repository.DocumentHistoryRepository;
import com.msdocument.repository.DocumentRepository;
import com.msdocument.repository.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentHistoryRepository historyRepository;
    private final DocumentAttachmentRepository attachmentRepository;

    @Transactional
    public DocumentDTO create(DocumentDTO dto) {
        DocumentType docType = documentTypeRepository.findById(dto.getDocumentTypeId())
                .orElseThrow(() -> new RuntimeException("Document type not found"));

        Document document = Document.builder()
                .documentNumber(generateDocumentNumber())
                .documentType(docType)
                .citizenId(dto.getCitizenId())
                .subject(dto.getSubject())
                .description(dto.getDescription())
                .status("DRAFT")
                .priority(dto.getPriority() != null ? dto.getPriority() : 1)
                .build();

        document = documentRepository.save(document);

        // Create history
        createHistory(document.getId(), "CREATED", "", "DRAFT",
                "Document created", dto.getCitizenId(), null, null);

        return toDTO(document);
    }

    @Transactional
    public DocumentDTO submit(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (!"DRAFT".equals(document.getStatus())) {
            throw new RuntimeException("Only draft documents can be submitted");
        }

        String previousStatus = document.getStatus();
        document.setStatus("SUBMITTED");
        document.setSubmittedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());
        document = documentRepository.save(document);

        createHistory(id, "SUBMITTED", previousStatus, "SUBMITTED",
                "Document submitted for processing", document.getCitizenId(), null, null);

        return toDTO(document);
    }

    @Transactional
    public DocumentDTO assign(DocumentAssignmentDTO dto) {
        Document document = documentRepository.findById(dto.getDocumentId())
                .orElseThrow(() -> new RuntimeException("Document not found"));

        Long previousAreaId = document.getCurrentAreaId();
        document.setCurrentAreaId(dto.getAreaId());
        document.setAssignedEmployeeId(dto.getEmployeeId());
        document.setStatus("IN_PROCESS");
        document.setUpdatedAt(LocalDateTime.now());
        document = documentRepository.save(document);

        createHistory(dto.getDocumentId(), "ASSIGNED", document.getStatus(), "IN_PROCESS",
                dto.getComments(), dto.getEmployeeId(), previousAreaId, dto.getAreaId());

        return toDTO(document);
    }

    @Transactional
    public DocumentDTO approve(Long id, String comments, Long employeeId) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        String previousStatus = document.getStatus();
        document.setStatus("APPROVED");
        document.setApprovedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());
        document = documentRepository.save(document);

        createHistory(id, "APPROVED", previousStatus, "APPROVED",
                comments, employeeId, document.getCurrentAreaId(), null);

        return toDTO(document);
    }

    @Transactional
    public DocumentDTO reject(Long id, String reason, Long employeeId) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        String previousStatus = document.getStatus();
        document.setStatus("REJECTED");
        document.setRejectionReason(reason);
        document.setRejectedAt(LocalDateTime.now());
        document.setUpdatedAt(LocalDateTime.now());
        document = documentRepository.save(document);

        createHistory(id, "REJECTED", previousStatus, "REJECTED",
                reason, employeeId, document.getCurrentAreaId(), null);

        return toDTO(document);
    }

    @Transactional(readOnly = true)
    public DocumentDTO getById(Long id) {
        return documentRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    @Transactional(readOnly = true)
    public Page<DocumentDTO> getByCitizen(Long citizenId, Pageable pageable) {
        return documentRepository.findByCitizenId(citizenId, pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<DocumentDTO> getByStatus(String status, Pageable pageable) {
        return documentRepository.findByStatus(status, pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<DocumentDTO> getByArea(Long areaId, Pageable pageable) {
        return documentRepository.findByCurrentAreaId(areaId, pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<DocumentDTO> getByEmployee(Long employeeId, Pageable pageable) {
        return documentRepository.findByAssignedEmployeeId(employeeId, pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<DocumentHistoryDTO> getHistory(Long documentId) {
        return historyRepository.findByDocumentIdOrderByCreatedAtDesc(documentId)
                .stream()
                .map(this::toHistoryDTO)
                .collect(Collectors.toList());
    }

    private String generateDocumentNumber() {
        int year = Year.now().getValue();
        long count = documentRepository.count() + 1;
        return String.format("DOC-%d-%05d", year, count);
    }

    private void createHistory(Long documentId, String action, String previousStatus,
                               String newStatus, String comments, Long performedBy,
                               Long fromAreaId, Long toAreaId) {
        DocumentHistory history = DocumentHistory.builder()
                .documentId(documentId)
                .action(action)
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .comments(comments)
                .performedBy(performedBy)
                .fromAreaId(fromAreaId)
                .toAreaId(toAreaId)
                .build();
        historyRepository.save(history);
    }

    private DocumentDTO toDTO(Document doc) {
        return DocumentDTO.builder()
                .id(doc.getId())
                .documentNumber(doc.getDocumentNumber())
                .documentTypeId(doc.getDocumentType().getId())
                .citizenId(doc.getCitizenId())
                .subject(doc.getSubject())
                .description(doc.getDescription())
                .status(doc.getStatus())
                .currentAreaId(doc.getCurrentAreaId())
                .assignedEmployeeId(doc.getAssignedEmployeeId())
                .priority(doc.getPriority())
                .submittedAt(doc.getSubmittedAt())
                .approvedAt(doc.getApprovedAt())
                .rejectedAt(doc.getRejectedAt())
                .rejectionReason(doc.getRejectionReason())
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }

    private DocumentHistoryDTO toHistoryDTO(DocumentHistory history) {
        return DocumentHistoryDTO.builder()
                .id(history.getId())
                .documentId(history.getDocumentId())
                .action(history.getAction())
                .previousStatus(history.getPreviousStatus())
                .newStatus(history.getNewStatus())
                .comments(history.getComments())
                .performedBy(history.getPerformedBy())
                .fromAreaId(history.getFromAreaId())
                .toAreaId(history.getToAreaId())
                .createdAt(history.getCreatedAt())
                .build();
    }
}
