package com.uroom.backend.controller;

import com.uroom.backend.domain.Room.RoomType;
import com.uroom.backend.dto.CreateRoomRequest;
import com.uroom.backend.dto.QuizQuestionRequest;
import com.uroom.backend.dto.QuizQuestionResponse;
import com.uroom.backend.dto.RoomMemberResponse;
import com.uroom.backend.dto.RoomResponse;
import com.uroom.backend.dto.JoinRoomRequest;
import com.uroom.backend.service.RoomService;
import com.uroom.backend.service.RoomMemberService;
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
    private final RoomMemberService roomMemberService;

    @PostMapping
    @Operation(summary = "Create room", description = "Creates a new room")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Room created"),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public RoomResponse create(@RequestBody @Valid CreateRoomRequest request) {
        return roomService.create(request);
    }

    @GetMapping("/{id}")
    public RoomResponse getById(@PathVariable UUID id) {
        return roomService.findById(id);
    }

    @GetMapping
    @Operation(summary = "Get all rooms", description = "Returns all rooms with optional filtering")
    public List<RoomResponse> getAll(
        @RequestParam(required = false) String filter,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String q,
        @RequestParam(required = false) String tag
    ) {
        if (q != null && !q.isBlank()) {
            return roomService.search(q);
        }
        if (tag != null && !tag.isBlank()) {
            return roomService.findByTag(tag);
        }
        RoomType roomType = type != null ? RoomType.valueOf(type.toUpperCase()) : null;
        return roomService.findByFilter(filter, roomType);
    }

    @PutMapping("/{id}")
    public RoomResponse update(@PathVariable UUID id, @RequestBody CreateRoomRequest request) {
        return roomService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete room", description = "Deletes a room by its UUID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Room deleted"),
        @ApiResponse(responseCode = "404", description = "Room not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/join")
    public RoomMemberResponse join(@PathVariable UUID id, 
                                   @RequestParam UUID userId,
                                   @RequestBody(required = false) JoinRoomRequest request) {
        return roomMemberService.joinRoom(id, userId, request);
    }

    @DeleteMapping("/{id}/leave")
    public ResponseEntity<Void> leave(@PathVariable UUID id, @RequestParam UUID userId) {
        roomMemberService.leaveRoom(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/members")
    public List<RoomMemberResponse> getMembers(@PathVariable UUID id) {
        return roomMemberService.getRoomMembers(id);
    }

    @GetMapping("/{id}/quiz-questions")
    public List<QuizQuestionResponse> getQuizQuestions(@PathVariable UUID id) {
        return roomService.getQuizQuestions(id);
    }

    @PostMapping("/{id}/quiz-questions")
    public QuizQuestionResponse addQuizQuestion(@PathVariable UUID id, 
                                                @RequestBody QuizQuestionRequest request) {
        return roomService.addQuizQuestion(id, request);
    }

    @GetMapping("/user/{userId}")
    public List<RoomResponse> getUserRooms(@PathVariable UUID userId) {
        return roomService.findByCreator(userId);
    }
}
