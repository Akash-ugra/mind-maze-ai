package com.lpu.mind_maze_ai.web.response.dto;

import lombok.Data;

/**
 * Data Transfer Object for quiz information.
 * Contains quiz metadata and status information.
 */
@Data
public class QuizDTO {
    /** Unique identifier for the quiz */
    private String quizId;

    /** Type of quiz (e.g., Math, Science) */
    private String quizType;

    /** Difficulty level of the quiz */
    private String quizLevel;

    /** Current status of quiz creation */
    private String creationStatus;

    /** Total number of questions in the quiz */
    private Integer numberOfQuestions;
}
