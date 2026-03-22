package com.uroom.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record QuizAnswerRequest(
    @NotNull
    UUID questionId,
    
    @NotBlank
    String answerText
){}
