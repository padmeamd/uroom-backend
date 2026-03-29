package com.uroom.backend.service;

import com.uroom.backend.config.JwtUtil;
import com.uroom.backend.domain.User;
import com.uroom.backend.dto.AuthResponse;
import com.uroom.backend.dto.LoginRequest;
import com.uroom.backend.dto.RegisterRequest;
import com.uroom.backend.dto.UserResponse;
import com.uroom.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User(request.name(), request.email(), passwordEncoder.encode(request.password()));
        user = userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId());
        return new AuthResponse(token, UserResponse.from(user));
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getId());
        return new AuthResponse(token, UserResponse.from(user));
    }

    public UserResponse getCurrentUser(User user) {
        return UserResponse.from(user);
    }
}
