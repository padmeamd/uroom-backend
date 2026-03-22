package com.uroom.backend.repository;

import com.uroom.backend.domain.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, UUID> {

    List<QuizQuestion> findByRoomIdOrderByOrderIndexAsc(UUID roomId);

    void deleteByRoomId(UUID roomId);
}
