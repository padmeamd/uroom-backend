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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "supabase_id", unique = true)
    private String supabaseId;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String university;

    private Integer age;

    @Column(name = "photo_url")
    private String photoUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_interests", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "interest")
    private List<String> interests = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_skills", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    @Column(length = 500)
    private String about;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @Column(name = "instagram_url")
    private String instagramUrl;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    private int level = 1;
    private int xp = 0;
    private int streak = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String supabaseId, String name, String email) {
        this.supabaseId = supabaseId;
        this.name = name;
        this.email = email;
    }

    public void addXp(int amount) {
        this.xp += amount;
        while (this.xp >= getXpForNextLevel()) {
            this.xp -= getXpForNextLevel();
            this.level++;
        }
    }

    private int getXpForNextLevel() {
        return level * 500;
    }
}
