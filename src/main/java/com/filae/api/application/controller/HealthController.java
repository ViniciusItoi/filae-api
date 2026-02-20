package com.filae.api.application.controller;

import com.filae.api.application.dto.system.SystemStatsResponse;
import com.filae.api.domain.repository.*;
import com.filae.api.infrastructure.logging.LogHelper;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller for system health and status endpoints
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    private static final Logger log = LogHelper.getLogger(HealthController.class);
    private static final String API_VERSION = "1.0.0";

    private final UserRepository userRepository;
    private final EstablishmentRepository establishmentRepository;
    private final QueueRepository queueRepository;
    private final NotificationRepository notificationRepository;
    private final FavoriteRepository favoriteRepository;

    public HealthController(UserRepository userRepository,
                           EstablishmentRepository establishmentRepository,
                           QueueRepository queueRepository,
                           NotificationRepository notificationRepository,
                           FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.establishmentRepository = establishmentRepository;
        this.queueRepository = queueRepository;
        this.notificationRepository = notificationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    /**
     * Health check endpoint - no authentication required
     */
    @GetMapping
    public ResponseEntity<String> health() {
        LogHelper.logMethodEntry(log, "health");
        LogHelper.logMethodExit(log, "health", "OK");
        return ResponseEntity.ok("Filae API is running!");
    }

    /**
     * System statistics endpoint
     */
    @GetMapping("/stats")
    public ResponseEntity<SystemStatsResponse> getSystemStats() {
        LogHelper.logMethodEntry(log, "getSystemStats");

        try {
            long totalUsers = userRepository.count();
            long totalEstablishments = establishmentRepository.count();
            long activeQueues = queueRepository.count();
            long totalNotifications = notificationRepository.count();
            long totalFavorites = favoriteRepository.count();

            SystemStatsResponse stats = SystemStatsResponse.builder()
                .status("UP")
                .totalUsers(totalUsers)
                .totalEstablishments(totalEstablishments)
                .activeQueues(activeQueues)
                .totalNotifications(totalNotifications)
                .totalFavorites(totalFavorites)
                .apiVersion(API_VERSION)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();

            LogHelper.logMethodExit(log, "getSystemStats", "stats retrieved");
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            LogHelper.logError(log, "Error retrieving system stats", e);
            SystemStatsResponse errorStats = SystemStatsResponse.builder()
                .status("ERROR")
                .apiVersion(API_VERSION)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();
            return ResponseEntity.internalServerError().body(errorStats);
        }
    }
}

