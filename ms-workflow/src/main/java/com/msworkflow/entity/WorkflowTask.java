package com.msworkflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "workflow_tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instance_id", nullable = false)
    private WorkflowInstance instance;

    @Column(nullable = false)
    private Long stepId; // Reference to WorkflowStep

    @Column(nullable = false)
    private Integer stepOrder;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Long areaId;

    @Column
    private Long assignedEmployeeId;

    @Column(nullable = false, length = 50)
    private String status; // PENDING, IN_PROGRESS, COMPLETED, SKIPPED

    @Column
    private LocalDateTime assignedAt;

    @Column
    private LocalDateTime startedAt;

    @Column
    private LocalDateTime completedAt;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @Column(columnDefinition = "TEXT")
    private String resolution;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
