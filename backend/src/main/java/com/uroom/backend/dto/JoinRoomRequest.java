package com.uroom.backend.dto;

import java.util.List;

public record JoinRoomRequest(
    String role,
    List<QuizAnswerRequest> quizAnswers
){}
