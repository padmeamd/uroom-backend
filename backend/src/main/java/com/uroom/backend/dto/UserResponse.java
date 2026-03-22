package com.uroom.backend.dto;

import com.uroom.backend.domain.Room;
import com.uroom.backend.domain.RoomMember;
import com.uroom.backend.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String email,
    String university,
    Integer age,
    String photoUrl,
    List<String> interests,
    List<String> skills,
    String about,
    String portfolioUrl,
    String instagramUrl,
    String githubUrl,
    String linkedinUrl,
    int level,
    int xp,
    int streak,
    LocalDateTime createdAt
){
    public static UserResponse from(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getUniversity(),
            user.getAge(),
            user.getPhotoUrl(),
            user.getInterests(),
            user.getSkills(),
            user.getAbout(),
            user.getPortfolioUrl(),
            user.getInstagramUrl(),
            user.getGithubUrl(),
            user.getLinkedinUrl(),
            user.getLevel(),
            user.getXp(),
            user.getStreak(),
            user.getCreatedAt()
        );
    }
}
