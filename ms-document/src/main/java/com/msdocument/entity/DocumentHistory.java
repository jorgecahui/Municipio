package com.msdocument.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long documentId;

    @Column(nullable = false, length = 50)
    private String action; // CREATED, SUBMITTED, ASSIGNED, APPROVED, REJECTED, etc.

    @Column(nullable = false, length = 50)
    private String previousStatus;

    @Column(nullable = false, length = 50)
    private String newStatus;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column
    private Long performedBy;

    @Column
    private Long fromAreaId;

    @Column
    private Long toAreaId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
