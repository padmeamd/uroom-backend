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
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "chat_id", nullable = false)
    private UUID chatId;

    @Column(name = "sender_id", nullable = false)
    private UUID senderId;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "sender_avatar")
    private String senderAvatar;

    @Column(length = 2000)
    private String text;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "location_label")
    private String locationLabel;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Message(UUID chatId, UUID senderId, String senderName, String senderAvatar, String text) {
        this.chatId = chatId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderAvatar = senderAvatar;
        this.text = text;
    }

    public void setLocation(Double lat, Double lng, String label) {
        this.latitude = lat;
        this.longitude = lng;
        this.locationLabel = label;
    }
}
