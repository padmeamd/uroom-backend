package com.uroom.backend.repository;

import com.uroom.backend.domain.RoomMember;
import com.uroom.backend.domain.RoomMember.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomMemberRepository extends JpaRepository<RoomMember, UUID> {

    List<RoomMember> findByRoomId(UUID roomId);

    List<RoomMember> findByUserId(UUID userId);

    Optional<RoomMember> findByRoomIdAndUserId(UUID roomId, UUID userId);

    boolean existsByRoomIdAndUserId(UUID roomId, UUID userId);

    List<RoomMember> findByRoomIdAndStatus(UUID roomId, MemberStatus status);

    int countByRoomId(UUID roomId);
}
