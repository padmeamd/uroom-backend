package com.uroom.backend.dto;

import com.uroom.backend.domain.QuizQuestion.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record QuizQuestionRequest(
    @NotNull
    QuestionType questionType,
    
    @NotBlank
    String question,
    
    List<String> options,
    
    boolean required,
    int orderIndex
){}
