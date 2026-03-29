package com.uroom.backend.service;

import com.uroom.backend.domain.Room;
import com.uroom.backend.domain.Room.RoomType;
import com.uroom.backend.domain.Chat;
import com.uroom.backend.domain.QuizQuestion;
import com.uroom.backend.dto.CreateRoomRequest;
import com.uroom.backend.dto.RoomResponse;
import com.uroom.backend.dto.QuizQuestionRequest;
import com.uroom.backend.dto.QuizQuestionResponse;
import com.uroom.backend.repository.ChatRepository;
import com.uroom.backend.repository.QuizQuestionRepository;
import com.uroom.backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;
    private final QuizQuestionRepository quizQuestionRepository;

    @Transactional
    public RoomResponse create(CreateRoomRequest request) {
        Room room = new Room(
            request.title(),
            request.roomType(),
            request.description(),
            request.maxMembers()
        );
        
        room.setBannerUrl(request.bannerUrl());
        room.setLocation(request.location());
        room.setDateTime(request.dateTime());
        room.setUniversity(request.university());
        room.setCreatorId(request.creatorId());
        room.setCreatorName(request.creatorName());
        room.setCreatorAvatar(request.creatorAvatar());
        if (request.tags() != null) {
            room.getTags().addAll(request.tags());
        }
        room.setUrgent(request.isUrgent());
        room.setQuizRequired(request.quizRequired());
        room.setAutoAccept(request.autoAccept());
        room.setInactivityTimeoutHours(request.inactivityTimeoutHours());
        
        Room saved = roomRepository.save(room);
        
        return RoomResponse.from(saved);
    }

    public List<RoomResponse> findAll() {
        return roomRepository.findAll()
                .stream()
                .map(RoomResponse::from)
                .toList();
    }

    public List<RoomResponse> findByFilter(String filter, RoomType type) {
        List<Room> rooms = roomRepository.findAll();
        
        if (type != null) {
            rooms = rooms.stream()
                .filter(r -> r.getRoomType() == type)
                .collect(Collectors.toList());
        }
        
        LocalDateTime now = LocalDateTime.now();
        
        switch (filter != null ? filter.toLowerCase() : "all") {
            case "this-week":
                rooms = rooms.stream()
                    .filter(r -> r.getDateTime() != null && r.getDateTime().isBefore(now.plusWeeks(1)))
                    .collect(Collectors.toList());
                break;
            case "quick-project":
                rooms = rooms.stream()
                    .filter(r -> r.getRoomType() == RoomType.PROJECT)
                    .filter(r -> r.getDateTime() != null && r.getDateTime().isBefore(now.plusDays(3)))
                    .collect(Collectors.toList());
                break;
            case "starting-soon":
                rooms = rooms.stream()
                    .filter(r -> r.getDateTime() != null && r.getDateTime().isBefore(now.plusDays(1)))
                    .collect(Collectors.toList());
                break;
            case "urgent":
                rooms = rooms.stream()
                    .filter(Room::isUrgent)
                    .collect(Collectors.toList());
                break;
            default:
                break;
        }
        
        return rooms.stream()
            .sorted((a, b) -> {
                if (a.isUrgent() && !b.isUrgent()) return -1;
                if (!a.isUrgent() && b.isUrgent()) return 1;
                return 0;
            })
            .map(RoomResponse::from)
            .collect(Collectors.toList());
    }

    public List<RoomResponse> search(String query) {
        if (query == null || query.isBlank()) {
            return findAll();
        }
        
        String lowerQuery = query.toLowerCase();
        return roomRepository.findAll().stream()
            .filter(r -> 
                (r.getTitle() != null && r.getTitle().toLowerCase().contains(lowerQuery)) ||
                (r.getDescription() != null && r.getDescription().toLowerCase().contains(lowerQuery)) ||
                r.getTags().stream().anyMatch(t -> t.toLowerCase().contains(lowerQuery))
            )
            .map(RoomResponse::from)
            .collect(Collectors.toList());
    }

    public RoomResponse findById(UUID id) {
        return roomRepository.findById(id)
                .map(RoomResponse::from)
                .orElseThrow(() ->
                        new IllegalArgumentException("Room not found: " + id)
                );
    }

    public List<RoomResponse> findByTag(String tag) {
        return roomRepository.findByTag(tag).stream()
            .map(RoomResponse::from)
            .collect(Collectors.toList());
    }

    public void deleteById(UUID id) {
        if (!roomRepository.existsById(id)) {
            throw new IllegalArgumentException("CANNOT FIND ROOM: " + id);
        }
        quizQuestionRepository.deleteByRoomId(id);
        roomRepository.deleteById(id);
    }

    @Transactional
    public RoomResponse update(UUID id, CreateRoomRequest request) {
        Room room = roomRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + id));
        
        room.setTitle(request.title());
        room.setRoomType(request.roomType());
        room.setDescription(request.description());
        room.setMaxMembers(request.maxMembers());
        room.setBannerUrl(request.bannerUrl());
        room.setLocation(request.location());
        room.setDateTime(request.dateTime());
        room.setUniversity(request.university());
        room.setUrgent(request.isUrgent());
        room.setQuizRequired(request.quizRequired());
        room.setAutoAccept(request.autoAccept());
        room.setInactivityTimeoutHours(request.inactivityTimeoutHours());
        if (request.tags() != null) {
            room.getTags().clear();
            room.getTags().addAll(request.tags());
        }
        
        return RoomResponse.from(roomRepository.save(room));
    }

    @Transactional
    public void incrementMemberCount(UUID roomId) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        room.setCurrentMembers(room.getCurrentMembers() + 1);
        roomRepository.save(room);
    }

    @Transactional
    public void decrementMemberCount(UUID roomId) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        room.setCurrentMembers(Math.max(0, room.getCurrentMembers() - 1));
        roomRepository.save(room);
    }

    public List<QuizQuestionResponse> getQuizQuestions(UUID roomId) {
        return quizQuestionRepository.findByRoomIdOrderByOrderIndexAsc(roomId)
            .stream()
            .map(q -> new QuizQuestionResponse(
                q.getId(),
                q.getRoomId(),
                q.getQuestionType(),
                q.getQuestion(),
                q.getOptions(),
                q.isRequired(),
                q.getOrderIndex()
            ))
            .collect(Collectors.toList());
    }

    @Transactional
    public QuizQuestionResponse addQuizQuestion(UUID roomId, QuizQuestionRequest request) {
        QuizQuestion question = new QuizQuestion(
            roomId,
            request.questionType(),
            request.question()
        );
        question.setOptions(request.options());
        question.setRequired(request.required());
        question.setOrderIndex(request.orderIndex());
        
        QuizQuestion saved = quizQuestionRepository.save(question);
        return new QuizQuestionResponse(
            saved.getId(),
            saved.getRoomId(),
            saved.getQuestionType(),
            saved.getQuestion(),
            saved.getOptions(),
            saved.isRequired(),
            saved.getOrderIndex()
        );
    }

    public List<RoomResponse> findByCreator(UUID creatorId) {
        return roomRepository.findByCreatorId(creatorId).stream()
            .map(RoomResponse::from)
            .collect(Collectors.toList());
    }
}
