package com.lpu.mind_maze_ai.web.response.dto;

import lombok.Data;

/**
 * Data Transfer Object for quiz score information.
 * Contains statistics about a user's quiz performance.
 */
@Data
public class ScoreDTO {
    /** Number of questions answered correctly */
    private Integer correctAnswers;

    /** Number of questions answered incorrectly */
    private Integer wrongAnswers;

    /** Total number of questions in the quiz */
    private Integer totalQuestions;
}
