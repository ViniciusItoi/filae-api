package com.filae.api.application.controller;

import com.filae.api.application.dto.notification.NotificationResponse;
import com.filae.api.application.mapper.NotificationMapper;
import com.filae.api.domain.service.NotificationService;
import com.filae.api.domain.service.UserService;
import com.filae.api.infrastructure.logging.LogHelper;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for notification management endpoints
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final Logger log = LogHelper.getLogger(NotificationController.class);

    private final NotificationService notificationService;
    private final UserService userService;
    private final NotificationMapper notificationMapper;

    public NotificationController(NotificationService notificationService,
                                 UserService userService,
                                 NotificationMapper notificationMapper) {
        this.notificationService = notificationService;
        this.userService = userService;
        this.notificationMapper = notificationMapper;
    }

    /**
     * Get authenticated user ID from security context
     */
    private Long getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String email = auth.getName();
            return userService.getUserByEmail(email).getId();
        }
        throw new IllegalStateException("User is not authenticated");
    }

    /**
     * Get user's notifications
     */
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @RequestParam(required = false) Boolean unread) {
        Long userId = getAuthenticatedUserId();

        LogHelper.logMethodEntry(log, "getNotifications", userId, "unread=" + unread);

        List<NotificationResponse> notifications;

        if (unread != null && unread) {
            notifications = notificationService.getUnreadNotifications(userId)
                .stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
        } else {
            notifications = notificationService.getUserNotifications(userId)
                .stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
        }

        LogHelper.logMethodExit(log, "getNotifications", notifications.size() + " found");
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get unread notification count
     */
    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadCount() {
        Long userId = getAuthenticatedUserId();

        LogHelper.logMethodEntry(log, "getUnreadCount", userId);

        Long count = notificationService.getUnreadCount(userId);

        LogHelper.logMethodExit(log, "getUnreadCount", count);
        return ResponseEntity.ok(count);
    }

    /**
     * Mark notification as read
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long id) {
        LogHelper.logMethodEntry(log, "markAsRead", id);

        var notification = notificationService.markAsRead(id);

        LogHelper.logMethodExit(log, "markAsRead", id);
        return ResponseEntity.ok(notificationMapper.toResponse(notification));
    }

    /**
     * Mark all notifications as read
     */
    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
        Long userId = getAuthenticatedUserId();

        LogHelper.logMethodEntry(log, "markAllAsRead", userId);

        notificationService.markAllAsRead(userId);

        LogHelper.logMethodExit(log, "markAllAsRead");
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete notification
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        LogHelper.logMethodEntry(log, "deleteNotification", id);

        notificationService.deleteNotification(id);

        LogHelper.logMethodExit(log, "deleteNotification");
        return ResponseEntity.noContent().build();
    }
}

