package com.uroom.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RoomMemberResponse(
    UUID id,
    UUID roomId,
    UUID userId,
    String role,
    String status,
    LocalDateTime joinedAt
){}
