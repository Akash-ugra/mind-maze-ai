package com.lpu.mind_maze_ai.web.response.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object for quiz questions.
 * Contains question details without revealing the correct answer.
 */
@Data
public class QuestionDTO {
    /** Unique identifier for the question */
    private UUID id;

    /** The question text */
    private String question;

    /** List of possible answer options */
    private List<String> options;
}
