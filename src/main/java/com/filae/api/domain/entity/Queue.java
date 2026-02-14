package com.filae.api.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Queue entity representing a customer's entry in an establishment's queue
 */
@Entity
@Table(name = "queues")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Queue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_number", unique = true, nullable = false)
    private String ticketNumber;

    @ManyToOne
    @JoinColumn(name = "establishment_id", nullable = false)
    private Establishment establishment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "party_size", nullable = false)
    private Integer partySize = 1;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private Integer position;

    @Column(name = "total_in_queue", nullable = false)
    private Integer totalInQueue;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QueueStatus status = QueueStatus.WAITING;

    @Column(name = "estimated_wait_time")
    private Integer estimatedWaitTime;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @Column(name = "called_at")
    private LocalDateTime calledAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum QueueStatus {
        WAITING,
        CALLED,
        FINISHED,
        CANCELLED
    }
}

