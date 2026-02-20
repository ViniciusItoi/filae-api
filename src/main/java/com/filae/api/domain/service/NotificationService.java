package com.filae.api.domain.service;

import com.filae.api.domain.entity.Notification;
import com.filae.api.domain.repository.NotificationRepository;
import com.filae.api.infrastructure.logging.LogHelper;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for notification management operations
 */
@Service
@Transactional
public class NotificationService {

    private static final Logger log = LogHelper.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Create and send a notification
     */
    public Notification createNotification(Notification notification) {
        LogHelper.logMethodEntry(log, "createNotification", notification.getUser().getId());

        Notification saved = notificationRepository.save(notification);
        LogHelper.logDatabaseOperation(log, "INSERT Notification", saved.getId());
        LogHelper.logMethodExit(log, "createNotification", saved.getId());

        return saved;
    }

    /**
     * Get user's notifications
     */
    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long userId) {
        LogHelper.logMethodEntry(log, "getUserNotifications", userId);

        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        LogHelper.logMethodExit(log, "getUserNotifications", notifications.size() + " found");

        return notifications;
    }

    /**
     * Get user's unread notifications
     */
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(Long userId) {
        LogHelper.logMethodEntry(log, "getUnreadNotifications", userId);

        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
        LogHelper.logMethodExit(log, "getUnreadNotifications", notifications.size() + " found");

        return notifications;
    }

    /**
     * Get count of unread notifications
     */
    @Transactional(readOnly = true)
    public Long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    /**
     * Mark notification as read
     */
    public Notification markAsRead(Long notificationId) {
        LogHelper.logMethodEntry(log, "markAsRead", notificationId);

        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new IllegalArgumentException("Notification not found with id: " + notificationId));

        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());

        Notification updated = notificationRepository.save(notification);
        LogHelper.logDatabaseOperation(log, "UPDATE Notification", updated.getId());
        LogHelper.logMethodExit(log, "markAsRead");

        return updated;
    }

    /**
     * Mark all notifications as read
     */
    public void markAllAsRead(Long userId) {
        LogHelper.logMethodEntry(log, "markAllAsRead", userId);

        notificationRepository.markAllAsRead(userId);
        LogHelper.logDatabaseOperation(log, "UPDATE All Notifications", userId);
        LogHelper.logMethodExit(log, "markAllAsRead");
    }

    /**
     * Delete notification
     */
    public void deleteNotification(Long notificationId) {
        LogHelper.logMethodEntry(log, "deleteNotification", notificationId);

        if (!notificationRepository.existsById(notificationId)) {
            throw new IllegalArgumentException("Notification not found with id: " + notificationId);
        }

        notificationRepository.deleteById(notificationId);
        LogHelper.logDatabaseOperation(log, "DELETE Notification", notificationId);
        LogHelper.logMethodExit(log, "deleteNotification");
    }
}

