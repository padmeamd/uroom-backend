package com.uroom.backend.controller;

import com.uroom.backend.dto.CreateRoomRequest;
import com.uroom.backend.dto.RoomResponse;
import com.uroom.backend.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @Operation(
            summary = "Create room",
            description = "Creates a new room"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Room created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    public RoomResponse create(@RequestBody @Valid CreateRoomRequest request) {
        return roomService.create(request);
    }

    @GetMapping("/{id}")
    public RoomResponse getById(@PathVariable UUID id) {
        return roomService.findById(id);
    }

    @Operation(
            summary = "Get all rooms",
            description = "Returns all rooms or filters them by tag"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rooms list returned")
    })
    @GetMapping
    public List<RoomResponse> getAll(
            @RequestParam(required = false) String tag
    ) {
        return roomService.findAll(tag);
    }

    @Operation(
            summary = "Delete room",
            description = "Deletes a room by its UUID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Room deleted"),
            @ApiResponse(responseCode = "404", description = "Room not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}