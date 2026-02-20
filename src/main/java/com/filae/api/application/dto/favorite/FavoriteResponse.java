package com.filae.api.application.dto.favorite;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Favorite response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteResponse {
    private Long id;
    private Long establishmentId;
    private String establishmentName;
    private String category;
    private String city;
    private Double rating;
    private LocalDateTime addedAt;
}

