package com.uroom.backend.dto;

public record SendMessageRequest(
    String text,
    LocationData location
){}
