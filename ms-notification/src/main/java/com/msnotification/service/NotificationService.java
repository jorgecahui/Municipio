package com.msnotification.service;

import com.msnotification.dto.NotificationDTO;
import com.msnotification.dto.SendNotificationRequest;
import com.msnotification.entity.Notification;
import com.msnotification.entity.NotificationTemplate;
import com.msnotification.repository.NotificationRepository;
import com.msnotification.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final JavaMailSender mailSender;

    @Transactional
    @Async
    public void sendNotification(SendNotificationRequest request) {
        NotificationTemplate template = templateRepository.findByCode(request.getTemplateCode())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        String subject = processTemplate(template.getSubject(), request.getVariables());
        String content = processTemplate(template.getTemplate(), request.getVariables());

        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .subject(subject)
                .content(content)
                .status("PENDING")
                .priority(request.getPriority() != null ? request.getPriority() : "NORMAL")
                .relatedEntityId(request.getRelatedEntityId())
                .relatedEntityType(request.getRelatedEntityType())
                .build();

        notification = notificationRepository.save(notification);

        // Send based on type
        try {
            switch (request.getType()) {
                case "EMAIL":
                    sendEmail(notification);
                    break;
                case "SMS":
                    sendSMS(notification);
                    break;
                case "IN_APP":
                    // Just save to DB
                    notification.setStatus("SENT");
                    notification.setSentAt(LocalDateTime.now());
                    break;
                default:
                    throw new RuntimeException("Unknown notification type");
            }
            notificationRepository.save(notification);
        } catch (Exception e) {
            log.error("Failed to send notification: {}", e.getMessage());
            notification.setStatus("FAILED");
            notification.setErrorMessage(e.getMessage());
            notification.setRetryCount(notification.getRetryCount() + 1);
            notificationRepository.save(notification);
        }
    }

    private void sendEmail(Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(notification.getRecipient());
        message.setSubject(notification.getSubject());
        message.setText(notification.getContent());

        mailSender.send(message);

        notification.setStatus("SENT");
        notification.setSentAt(LocalDateTime.now());
    }

    private void sendSMS(Notification notification) {
        // Integration with SMS provider (Twilio, etc.)
        log.info("Sending SMS to: {}", notification.getRecipient());
        notification.setStatus("SENT");
        notification.setSentAt(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<NotificationDTO> getUnreadNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdAndStatus(userId, "SENT", pageable)
                .map(this::toDTO);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setStatus("READ");
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public Long getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByUser(userId);
    }

    private String processTemplate(String template, Map<String, String> variables) {
        if (variables == null) return template;

        String result = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }

    private NotificationDTO toDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .type(notification.getType())
                .subject(notification.getSubject())
                .content(notification.getContent())
                .recipient(notification.getRecipient())
                .status(notification.getStatus())
                .priority(notification.getPriority())
                .relatedEntityId(notification.getRelatedEntityId())
                .relatedEntityType(notification.getRelatedEntityType())
                .createdAt(notification.getCreatedAt())
                .sentAt(notification.getSentAt())
                .readAt(notification.getReadAt())
                .build();
    }
}
