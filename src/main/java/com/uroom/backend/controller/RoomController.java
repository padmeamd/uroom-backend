package com.uroom.backend.controller;

import com.uroom.backend.dto.CreateRoomRequest;
import com.uroom.backend.dto.RoomResponse;
import com.uroom.backend.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public RoomResponse create(@RequestBody @Valid CreateRoomRequest request) {
        return roomService.create(request);
    }

    @GetMapping("/{id}")
    public RoomResponse getById(@PathVariable UUID id) {
        return roomService.findById(id);
    }

    @GetMapping
    public List<RoomResponse> getAll(
            @RequestParam(required = false) String tag
    ) {
        return roomService.findAll(tag);
    }
}