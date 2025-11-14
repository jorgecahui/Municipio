package com.msworkflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "workflow_instances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long documentId; // Reference to ms-document

    @Column(nullable = false)
    private Long templateId;

    @Column(nullable = false)
    private Integer currentStep = 0;

    @Column(nullable = false, length = 50)
    private String status; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime completedAt;

    @Column
    private LocalDateTime cancelledAt;

    @Column(columnDefinition = "TEXT")
    private String cancellationReason;

    @OneToMany(mappedBy = "instance", cascade = CascadeType.ALL)
    private List<WorkflowTask> tasks;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
}
