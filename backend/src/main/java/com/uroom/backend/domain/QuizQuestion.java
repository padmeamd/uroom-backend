package com.uroom.backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "quiz_questions")
public class QuizQuestion {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "room_id", nullable = false)
    private UUID roomId;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    @Column(nullable = false, length = 500)
    private String question;

    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_text")
    private List<String> options = new ArrayList<>();

    @Column(name = "is_required")
    private boolean required = true;

    private int orderIndex = 0;

    public enum QuestionType {
        TEXT, SINGLE_CHOICE, MULTIPLE_CHOICE
    }

    public QuizQuestion(UUID roomId, QuestionType questionType, String question) {
        this.roomId = roomId;
        this.questionType = questionType;
        this.question = question;
    }
}
