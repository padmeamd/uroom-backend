package com.uroom.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
    String supabaseId,
    
    @NotBlank
    String name,
    
    @NotBlank
    @Email
    String email
) {}
