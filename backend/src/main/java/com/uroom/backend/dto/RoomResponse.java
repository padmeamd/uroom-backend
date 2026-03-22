package com.uroom.backend.dto;

import com.uroom.backend.domain.Room;
import com.uroom.backend.domain.Room.RoomType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RoomResponse(
    UUID id,
    String title,
    RoomType roomType,
    String bannerUrl,
    String description,
    String location,
    LocalDateTime dateTime,
    String university,
    UUID creatorId,
    String creatorName,
    String creatorAvatar,
    List<String> tags,
    boolean isUrgent,
    int maxMembers,
    int currentMembers,
    boolean quizRequired,
    boolean autoAccept,
    Integer inactivityTimeoutHours,
    LocalDateTime createdAt
){
    public static RoomResponse from(Room room) {
        return new RoomResponse(
            room.getId(),
            room.getTitle(),
            room.getRoomType(),
            room.getBannerUrl(),
            room.getDescription(),
            room.getLocation(),
            room.getDateTime(),
            room.getUniversity(),
            room.getCreatorId(),
            room.getCreatorName(),
            room.getCreatorAvatar(),
            room.getTags(),
            room.isUrgent(),
            room.getMaxMembers(),
            room.getCurrentMembers(),
            room.isQuizRequired(),
            room.isAutoAccept(),
            room.getInactivityTimeoutHours(),
            room.getCreatedAt()
        );
    }
}
