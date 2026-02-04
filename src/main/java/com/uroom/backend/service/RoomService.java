package com.uroom.backend.service;

import com.uroom.backend.domain.Room;
import com.uroom.backend.dto.CreateRoomRequest;
import com.uroom.backend.dto.RoomResponse;
import com.uroom.backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomResponse create(CreateRoomRequest request) {
        Room room = new Room(
                request.title(),
                request.description(),
                request.membersLimit(),
                request.tags()
        );

        Room saved = roomRepository.save(room);
        return toResponse(saved);
    }

    public List<RoomResponse> findAll() {
        return roomRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private RoomResponse toResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getTitle(),
                room.getDescription(),
                room.getMembersLimit(),
                room.getTags()
        );
    }

    public RoomResponse findById(UUID id) {
        return roomRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() ->
                        new IllegalArgumentException("Room not found: " + id)
                );
    }
    public List<RoomResponse> findAll(String tag) {

        List<Room> rooms = (tag == null || tag.isBlank())
                ? roomRepository.findAll()
                : roomRepository.findByTag(tag);

        return rooms.stream()
                .map(this::toResponse)
                .toList();
    }

    public void deleteById(UUID id) {
        if (!roomRepository.existsById(id)) {
            throw new IllegalArgumentException("CANNOT FIND ROOM: " + id);
        }
        roomRepository.deleteById(id);
    }
}
