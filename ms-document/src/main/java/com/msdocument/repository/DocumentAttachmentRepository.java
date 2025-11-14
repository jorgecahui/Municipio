package com.msdocument.repository;

import com.msdocument.entity.DocumentAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentAttachmentRepository extends JpaRepository<DocumentAttachment, Long> {
    List<DocumentAttachment> findByDocumentId(Long documentId);
    void deleteByDocumentId(Long documentId);
}
