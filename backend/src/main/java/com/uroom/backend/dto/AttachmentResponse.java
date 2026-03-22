package com.uroom.backend.dto;

import com.uroom.backend.domain.MessageAttachment.AttachmentType;

import java.util.UUID;

public record AttachmentResponse(
    UUID id,
    String url,
    String name,
    AttachmentType type,
    String mimeType,
    long size
){}
