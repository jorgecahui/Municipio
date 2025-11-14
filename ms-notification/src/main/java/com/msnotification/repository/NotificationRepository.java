package com.msnotification.repository;

import com.msnotification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserId(Long userId, Pageable pageable);
    Page<Notification> findByUserIdAndStatus(Long userId, String status, Pageable pageable);
    List<Notification> findByStatus(String status);

    @Query("SELECT n FROM Notification n WHERE n.status = 'PENDING' AND n.retryCount < 3")
    List<Notification> findPendingNotifications();

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.status = 'PENDING'")
    Long countUnreadByUser(Long userId);

    List<Notification> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
