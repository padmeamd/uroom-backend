package com.uroom.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record UpdateUserRequest(
    @NotBlank
    String name,
    
    String university,
    Integer age,
    String photoUrl,
    
    List<String> interests,
    List<String> skills,
    
    @Size(max = 500)
    String about,
    
    String portfolioUrl,
    String instagramUrl,
    String githubUrl,
    String linkedinUrl
){}
