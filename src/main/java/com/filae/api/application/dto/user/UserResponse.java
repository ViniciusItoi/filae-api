package com.filae.api.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for User response (without password)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String userType;
    private String profilePictureUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

