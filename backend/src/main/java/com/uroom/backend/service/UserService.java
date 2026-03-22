package com.uroom.backend.service;

import com.uroom.backend.domain.User;
import com.uroom.backend.dto.UpdateUserRequest;
import com.uroom.backend.dto.UserResponse;
import com.uroom.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse findById(UUID id) {
        return userRepository.findById(id)
            .map(UserResponse::from)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    public UserResponse findByEmail(String email) {
        return userRepository.findByEmail(email)
            .map(UserResponse::from)
            .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
            .map(UserResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse create(String name, String email) {
        User user = new User(name, email);
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse update(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        
        user.setName(request.name());
        user.setUniversity(request.university());
        user.setAge(request.age());
        user.setPhotoUrl(request.photoUrl());
        user.setAbout(request.about());
        user.setPortfolioUrl(request.portfolioUrl());
        user.setInstagramUrl(request.instagramUrl());
        user.setGithubUrl(request.githubUrl());
        user.setLinkedinUrl(request.linkedinUrl());
        
        if (request.interests() != null) {
            user.getInterests().clear();
            user.getInterests().addAll(request.interests());
        }
        if (request.skills() != null) {
            user.getSkills().clear();
            user.getSkills().addAll(request.skills());
        }
        
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public void addXp(UUID userId, int xpAmount) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        user.addXp(xpAmount);
        userRepository.save(user);
    }

    @Transactional
    public void updateStreak(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        user.setStreak(user.getStreak() + 1);
        userRepository.save(user);
    }

    public void deleteById(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }
}
