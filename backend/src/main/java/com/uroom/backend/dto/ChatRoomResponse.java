package com.uroom.backend.dto;

import com.uroom.backend.domain.Room.RoomType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ChatRoomResponse(
    UUID id,
    UUID roomId,
    String roomTitle,
    RoomType roomType,
    int memberCount,
    List<String> memberAvatars,
    LocalDateTime createdAt
){}
