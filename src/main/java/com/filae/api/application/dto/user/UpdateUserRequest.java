package com.filae.api.application.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Update User request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Phone is required")
    @Size(min = 10, max = 20, message = "Phone must be between 10 and 20 characters")
    private String phone;

    private String profilePictureUrl;
}

