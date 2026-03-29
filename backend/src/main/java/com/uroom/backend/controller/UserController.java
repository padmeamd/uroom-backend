package com.uroom.backend.controller;

import com.uroom.backend.dto.CreateUserRequest;
import com.uroom.backend.dto.UpdateUserRequest;
import com.uroom.backend.dto.UserResponse;
import com.uroom.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable UUID id) {
        return userService.findById(id);
    }

    @GetMapping("/email/{email}")
    public UserResponse getByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @GetMapping("/supabase/{supabaseId}")
    public UserResponse getBySupabaseId(@PathVariable String supabaseId) {
        return userService.findBySupabaseId(supabaseId);
    }

    @GetMapping
    public List<UserResponse> getAll() {
        return userService.findAll();
    }

    @PostMapping
    public UserResponse create(@RequestBody @Valid CreateUserRequest request) {
        return userService.create(request.supabaseId(), request.name(), request.email());
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable UUID id, @RequestBody @Valid UpdateUserRequest request) {
        return userService.update(id, request);
    }

    @PostMapping("/{id}/xp")
    public ResponseEntity<Void> addXp(@PathVariable UUID id, @RequestParam int amount) {
        userService.addXp(id, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/streak")
    public ResponseEntity<Void> updateStreak(@PathVariable UUID id) {
        userService.updateStreak(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
