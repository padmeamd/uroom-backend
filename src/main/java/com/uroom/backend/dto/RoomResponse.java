package com.uroom.backend.dto;

import java.util.List;
import java.util.UUID;

public record RoomResponse (
        UUID id,
        String title,
        String description,
        int membersLimit,
        List<String> tags
){
}
