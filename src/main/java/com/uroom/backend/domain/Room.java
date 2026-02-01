package com.uroom.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int membersLimit;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "room_tags", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "tag")
    private List<String> tags;

    public Room(String title, String description, int membersLimit, List<String> tags) {
        this.title = title;
        this.description = description;
        this.membersLimit = membersLimit;
        this.tags = tags;
    }
}
