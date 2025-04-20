package com.lpu.mind_maze_ai.web.response.dto;

import lombok.Data;

/**
 * Data Transfer Object for quiz answer responses.
 * Provides feedback about answer correctness and reveals correct answer.
 */
@Data
public class CorrectResponseDTO {
    /** Whether the user's answer was correct */
    private Boolean isCorrect;

    /** The correct answer option */
    private String correctOption;

    /** The question text for reference */
    private String question;
}
