package com.uroom.backend.dto;

public record AuthResponse(
    String token,
    UserResponse user
) {}
