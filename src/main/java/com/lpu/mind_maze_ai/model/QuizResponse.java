package com.lpu.mind_maze_ai.model;

import lombok.Data;

import java.util.List;

/**
 * Model class representing the AI-generated quiz response.
 * Maps the structured JSON response from the AI service.
 */
@Data
public class QuizResponse {
    /** Unique identifier of the quiz */
    private String quizId;

    /** Type of quiz generated */
    private String quizType;

    /** Difficulty level of the quiz */
    private String quizLevel;

    /** Number of questions in the quiz */
    private Integer numberOfQuestions;

    /** List of generated questions with answers */
    private List<QuizQuestionDTO> quizQuestionList;
}
