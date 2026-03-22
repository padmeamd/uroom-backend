package com.uroom.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "message_attachments")
public class MessageAttachment {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "message_id", nullable = false)
    private UUID messageId;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "file_name")
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "attachment_type")
    private AttachmentType attachmentType;

    @Column(name = "mime_type")
    private String mimeType;

    private long size;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum AttachmentType {
        IMAGE, FILE
    }

    public MessageAttachment(UUID messageId, String fileUrl, String fileName, AttachmentType type, String mimeType, long size) {
        this.messageId = messageId;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.attachmentType = type;
        this.mimeType = mimeType;
        this.size = size;
    }
}
