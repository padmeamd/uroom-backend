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
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "room_id", nullable = false, unique = true)
    private UUID roomId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Chat(UUID roomId) {
        this.roomId = roomId;
    }
}
