package com.uroom.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType roomType;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(length = 1000)
    private String description;

    private String location;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    private String university;

    @Column(name = "creator_id")
    private UUID creatorId;

    @Column(name = "creator_name")
    private String creatorName;

    @Column(name = "creator_avatar")
    private String creatorAvatar;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "room_tags", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Column(name = "is_urgent")
    private boolean urgent = false;

    @Column(name = "members_limit")
    private int maxMembers;

    @Column(name = "current_members")
    private int currentMembers = 0;

    @Column(name = "quiz_required")
    private boolean quizRequired = false;

    @Column(name = "auto_accept")
    private boolean autoAccept = true;

    @Column(name = "inactivity_timeout_hours")
    private Integer inactivityTimeoutHours;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum RoomType {
        EVENT, PROJECT
    }

    public Room(String title, RoomType roomType, String description, int maxMembers) {
        this.title = title;
        this.roomType = roomType;
        this.description = description;
        this.maxMembers = maxMembers;
    }

    public boolean isFull() {
        return currentMembers >= maxMembers;
    }
}
