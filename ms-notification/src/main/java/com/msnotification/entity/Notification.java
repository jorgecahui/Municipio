package com.msnotification.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // Reference to ms-auth User

    @Column(nullable = false, length = 50)
    private String type; // EMAIL, SMS, PUSH, IN_APP

    @Column(nullable = false, length = 200)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 100)
    private String recipient; // Email or phone number

    @Column(nullable = false, length = 50)
    private String status; // PENDING, SENT, FAILED, READ

    @Column
    private LocalDateTime sentAt;

    @Column
    private LocalDateTime readAt;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @Column
    private Integer retryCount = 0;

    @Column(length = 50)
    private String priority; // LOW, NORMAL, HIGH

    @Column
    private Long relatedEntityId; // Document ID, Workflow ID, etc.

    @Column(length = 50)
    private String relatedEntityType; // DOCUMENT, WORKFLOW, USER

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
