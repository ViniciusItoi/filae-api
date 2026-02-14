package com.filae.api.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Establishment entity representing restaurants, barbershops, clinics, etc.
 */
@Entity
@Table(name = "establishments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Establishment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false, length = 2)
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    private Double latitude;

    private Double longitude;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(unique = true)
    private String email;

    private String website;

    @Column(name = "photo_url")
    private String photoUrl;

    private Double rating = 0.0;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @Column(name = "is_accepting_customers")
    private Boolean isAcceptingCustomers = true;

    @Column(name = "queue_enabled")
    private Boolean queueEnabled = true;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private User merchant;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

