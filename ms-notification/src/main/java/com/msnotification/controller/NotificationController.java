package com.msnotification.controller;

import com.msnotification.dto.NotificationDTO;
import com.msnotification.dto.SendNotificationRequest;
import com.msnotification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<Void> send(@Valid @RequestBody SendNotificationRequest request) {
        notificationService.sendNotification(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<NotificationDTO>> getUserNotifications(
            @PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId, pageable));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<Page<NotificationDTO>> getUnreadNotifications(
            @PathVariable Long userId, Pageable pageable) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId, pageable));
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadCount(userId));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}
