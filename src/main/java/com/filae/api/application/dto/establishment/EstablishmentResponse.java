package com.filae.api.application.dto.establishment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Establishment response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstablishmentResponse {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String phone;
    private String category;
    private Double rating;
    private Integer reviewCount;
    private Integer currentWaitTime;
    private Integer estimatedServeTime;
    private Boolean queueEnabled;
    private Boolean isAcceptingCustomers;
    private Integer maxCapacity;
    private Integer currentInQueue;
}

