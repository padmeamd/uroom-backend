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
@Table(name = "room_members", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"room_id", "user_id"})
})
public class RoomMember {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "room_id", nullable = false)
    private UUID roomId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    private String role;

    @Enumerated(EnumType.STRING)
    private MemberStatus status = MemberStatus.ACTIVE;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt = LocalDateTime.now();

    @Column(name = "last_active_at")
    private LocalDateTime lastActiveAt = LocalDateTime.now();

    public enum MemberStatus {
        ACTIVE, PENDING, REJECTED, KICKED
    }

    public RoomMember(UUID roomId, UUID userId, String role) {
        this.roomId = roomId;
        this.userId = userId;
        this.role = role;
    }

    public void updateLastActive() {
        this.lastActiveAt = LocalDateTime.now();
    }
}
