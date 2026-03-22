package com.uroom.backend.repository;

import com.uroom.backend.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByChatIdOrderByCreatedAtAsc(UUID chatId);

    @Query("""
        select m from Message m
        where m.chatId = :chatId
        order by m.createdAt desc
    """)
    List<Message> findRecentMessagesByChatId(@Param("chatId") UUID chatId, org.springframework.data.domain.Pageable pageable);

    int countByChatId(UUID chatId);
}
