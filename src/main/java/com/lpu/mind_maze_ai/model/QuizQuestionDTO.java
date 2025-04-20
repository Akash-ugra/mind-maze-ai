package com.lpu.mind_maze_ai.model;

import lombok.Data;

import java.util.List;

/**
 * Model class representing a single question in the AI-generated quiz.
 * Used for initial question creation before persistence.
 */
@Data
public class QuizQuestionDTO {
    /** The question text */
    private String question;

    /** List of possible answer options */
    private List<String> options;

    /** The correct answer from the options */
    private String answer;
}
