package com.uroom.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateRoomRequest(

    @NotBlank
    String title,

    @Size(max = 1000)
    String description,

    @NotNull
    Integer membersLimit,

    List<String> tags
){}
