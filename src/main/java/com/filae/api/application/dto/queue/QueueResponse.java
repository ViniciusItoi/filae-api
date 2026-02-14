package com.filae.api.application.dto.queue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Queue response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueResponse {
    private Long id;
    private String ticketNumber;
    private Long establishmentId;
    private String establishmentName;
    private Long userId;
    private String userName;
    private Integer partySize;
    private String notes;
    private Integer position;
    private Integer totalInQueue;
    private String status;
    private Integer estimatedWaitTime;
    private LocalDateTime joinedAt;
    private LocalDateTime calledAt;
    private LocalDateTime finishedAt;
}

