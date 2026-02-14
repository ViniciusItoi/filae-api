package com.filae.api.application.dto.favorite;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Add Favorite request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddFavoriteRequest {

    @NotNull(message = "Establishment ID is required")
    private Long establishmentId;
}

