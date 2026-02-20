package com.filae.api.application.dto.queue;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Queue request to join
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinQueueRequest {

    @NotNull(message = "Establishment ID is required")
    private Long establishmentId;

    @Min(value = 1, message = "Party size must be at least 1")
    private Integer partySize = 1;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
}

