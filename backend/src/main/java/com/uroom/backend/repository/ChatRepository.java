package com.uroom.backend.repository;

import com.uroom.backend.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

    Optional<Chat> findByRoomId(UUID roomId);
}
