package com.uroom.backend.dto;

import com.uroom.backend.domain.QuizQuestion.QuestionType;
import java.util.List;
import java.util.UUID;

public record QuizQuestionResponse(
    UUID id,
    UUID roomId,
    QuestionType questionType,
    String question,
    List<String> options,
    boolean required,
    int orderIndex
){}
