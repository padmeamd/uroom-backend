package com.uroom.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "quiz_answers")
public class QuizAnswer {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "room_id", nullable = false)
    private UUID roomId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "question_id", nullable = false)
    private UUID questionId;

    @Column(name = "answer_text", length = 1000)
    private String answerText;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt = LocalDateTime.now();

    public QuizAnswer(UUID roomId, UUID userId, UUID questionId, String answerText) {
        this.roomId = roomId;
        this.userId = userId;
        this.questionId = questionId;
        this.answerText = answerText;
    }
}
