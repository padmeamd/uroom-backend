package com.uroom.backend.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
    UUID id,
    UUID senderId,
    String senderName,
    String senderAvatar,
    String text,
    List<AttachmentResponse> attachments,
    LocationData location,
    boolean isCurrentUser,
    LocalDateTime createdAt
){}
