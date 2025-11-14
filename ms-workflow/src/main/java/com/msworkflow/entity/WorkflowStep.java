package com.msworkflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "workflow_steps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private WorkflowTemplate template;

    @Column(nullable = false)
    private Integer stepOrder;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Long areaId; // Area responsible for this step

    @Column(nullable = false)
    private Integer estimatedDays;

    @Column(nullable = false)
    private Boolean requiresApproval = true;

    @Column(nullable = false)
    private Boolean isOptional = false;
}
