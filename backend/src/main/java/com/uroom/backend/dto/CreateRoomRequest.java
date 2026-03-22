package com.uroom.backend.dto;

import com.uroom.backend.domain.Room.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateRoomRequest(
    @NotBlank
    String title,

    @NotNull
    RoomType roomType,

    String bannerUrl,

    @Size(max = 1000)
    String description,

    String location,
    LocalDateTime dateTime,
    String university,
    
    @NotNull
    Integer maxMembers,

    List<String> tags,

    boolean isUrgent,
    boolean quizRequired,
    boolean autoAccept,
    Integer inactivityTimeoutHours,

    UUID creatorId,
    String creatorName,
    String creatorAvatar
){}
