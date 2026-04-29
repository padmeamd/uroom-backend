package com.uroom.backend.service;

import com.uroom.backend.domain.Room;
import com.uroom.backend.domain.RoomMember;
import com.uroom.backend.domain.RoomMember.MemberStatus;
import com.uroom.backend.domain.User;
import com.uroom.backend.dto.JoinRoomRequest;
import com.uroom.backend.dto.MemberWithProfileResponse;
import com.uroom.backend.dto.QuizAnswerRequest;
import com.uroom.backend.dto.RoomMemberResponse;
import com.uroom.backend.repository.RoomMemberRepository;
import com.uroom.backend.repository.RoomRepository;
import com.uroom.backend.repository.UserRepository;
import com.uroom.backend.repository.QuizAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomMemberService {

    private final RoomMemberRepository roomMemberRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final QuizAnswerRepository quizAnswerRepository;

    @Transactional
    public RoomMemberResponse joinRoom(UUID roomId, UUID userId, JoinRoomRequest request) {
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        
        if (room.isFull()) {
            throw new IllegalStateException("Room is full");
        }
        
        if (roomMemberRepository.existsByRoomIdAndUserId(roomId, userId)) {
            throw new IllegalStateException("Already a member of this room");
        }
        
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        MemberStatus status = room.isAutoAccept() ? MemberStatus.ACTIVE : MemberStatus.PENDING;
        
        RoomMember member = new RoomMember(roomId, userId, request != null ? request.role() : null);
        member.setStatus(status);
        
        RoomMember saved = roomMemberRepository.save(member);
        
        if (status == MemberStatus.ACTIVE) {
            room.setCurrentMembers(room.getCurrentMembers() + 1);
            roomRepository.save(room);
            
            user.addXp(50);
            userRepository.save(user);
        }
        
        if (request != null && request.quizAnswers() != null) {
            for (QuizAnswerRequest answer : request.quizAnswers()) {
                com.uroom.backend.domain.QuizAnswer quizAnswer = 
                    new com.uroom.backend.domain.QuizAnswer(roomId, userId, answer.questionId(), answer.answerText());
                quizAnswerRepository.save(quizAnswer);
            }
        }
        
        return toResponse(saved);
    }

    @Transactional
    public void leaveRoom(UUID roomId, UUID userId) {
        RoomMember member = roomMemberRepository.findByRoomIdAndUserId(roomId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Not a member of this room"));
        
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        
        roomMemberRepository.delete(member);
        
        room.setCurrentMembers(Math.max(0, room.getCurrentMembers() - 1));
        roomRepository.save(room);
    }

    public List<RoomMemberResponse> getRoomMembers(UUID roomId) {
        return roomMemberRepository.findByRoomId(roomId).stream()
            .filter(m -> m.getStatus() == MemberStatus.ACTIVE)
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public List<MemberWithProfileResponse> getRoomMembersWithProfiles(UUID roomId) {
        return roomMemberRepository.findByRoomId(roomId).stream()
            .filter(m -> m.getStatus() == MemberStatus.ACTIVE)
            .map(m -> toProfileResponse(m, roomId))
            .collect(Collectors.toList());
    }

    public List<MemberWithProfileResponse> getPendingMembers(UUID roomId) {
        return roomMemberRepository.findByRoomId(roomId).stream()
            .filter(m -> m.getStatus() == MemberStatus.PENDING)
            .map(m -> toProfileResponse(m, roomId))
            .collect(Collectors.toList());
    }

    @Transactional
    public void acceptMember(UUID roomId, UUID userId) {
        RoomMember member = roomMemberRepository.findByRoomIdAndUserId(roomId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        member.setStatus(MemberStatus.ACTIVE);
        roomMemberRepository.save(member);

        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        room.setCurrentMembers(room.getCurrentMembers() + 1);
        roomRepository.save(room);

        userRepository.findById(userId).ifPresent(user -> {
            user.addXp(50);
            userRepository.save(user);
        });
    }

    @Transactional
    public void rejectMember(UUID roomId, UUID userId) {
        RoomMember member = roomMemberRepository.findByRoomIdAndUserId(roomId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Member not found"));
        member.setStatus(MemberStatus.REJECTED);
        roomMemberRepository.save(member);
    }

    public List<RoomMemberResponse> getUserRooms(UUID userId) {
        return roomMemberRepository.findByUserId(userId).stream()
            .filter(m -> m.getStatus() == MemberStatus.ACTIVE)
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public boolean isMember(UUID roomId, UUID userId) {
        return roomMemberRepository.findByRoomIdAndUserId(roomId, userId)
            .map(m -> m.getStatus() == MemberStatus.ACTIVE)
            .orElse(false);
    }

    public RoomMemberResponse getMember(UUID roomId, UUID userId) {
        return roomMemberRepository.findByRoomIdAndUserId(roomId, userId)
            .map(this::toResponse)
            .orElse(null);
    }

    @Transactional
    public void updateLastActive(UUID roomId, UUID userId) {
        roomMemberRepository.findByRoomIdAndUserId(roomId, userId)
            .ifPresent(RoomMember::updateLastActive);
    }

    @Transactional
    public void kickMember(UUID roomId, UUID userId) {
        RoomMember member = roomMemberRepository.findByRoomIdAndUserId(roomId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Not a member of this room"));
        
        member.setStatus(MemberStatus.KICKED);
        roomMemberRepository.save(member);
        
        Room room = roomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("Room not found: " + roomId));
        room.setCurrentMembers(Math.max(0, room.getCurrentMembers() - 1));
        roomRepository.save(room);
    }

    private RoomMemberResponse toResponse(RoomMember member) {
        return new RoomMemberResponse(
            member.getId(),
            member.getRoomId(),
            member.getUserId(),
            member.getRole(),
            member.getStatus().name(),
            member.getJoinedAt()
        );
    }

    private MemberWithProfileResponse toProfileResponse(RoomMember member, UUID roomId) {
        return userRepository.findById(member.getUserId())
            .map(user -> new MemberWithProfileResponse(
                user.getId(),
                user.getName(),
                user.getPhotoUrl(),
                user.getUniversity(),
                user.getAge(),
                user.getAbout(),
                user.getInterests(),
                user.getSkills(),
                user.getLevel(),
                user.getXp(),
                user.getStreak(),
                member.getRole(),
                member.getStatus().name(),
                member.getJoinedAt()
            ))
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + member.getUserId()));
    }
}
