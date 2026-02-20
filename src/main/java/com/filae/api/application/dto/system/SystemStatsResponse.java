package com.filae.api.application.dto.system;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for system health and statistics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemStatsResponse {
    private String status;
    private Long totalUsers;
    private Long totalEstablishments;
    private Long activeQueues;
    private Long totalNotifications;
    private Long totalFavorites;
    private String apiVersion;
    private String timestamp;
}

