package com.msdocument.repository;

import com.msdocument.entity.DocumentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory, Long> {
    List<DocumentHistory> findByDocumentIdOrderByCreatedAtDesc(Long documentId);
    Page<DocumentHistory> findByDocumentId(Long documentId, Pageable pageable);
}
