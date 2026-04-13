package com.uroom.backend.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MemberWithProfileResponse(
    UUID userId,
    String name,
    String photoUrl,
    String university,
    Integer age,
    String about,
    List<String> interests,
    List<String> skills,
    int level,
    int xp,
    int streak,
    String role,
    String status,
    LocalDateTime joinedAt
) {}
