package com.lpu.mind_maze_ai.web.request.dto;

import com.lpu.mind_maze_ai.model.QuizLevel;
import com.lpu.mind_maze_ai.model.QuizType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object for quiz creation requests.
 * Contains parameters needed to generate a new quiz.
 */
@Data
public class CreateQuizDTO {
    /** Type of quiz to create */
    @NotNull(message = "Quiz type cannot be null")
    private QuizType quizType;

    /** Difficulty level for the quiz */
    @NotNull(message = "Quiz level cannot be null")
    private QuizLevel quizLevel;

    /** Number of questions to generate */
    private Integer numberOfQuestions;

    /**
     * Creates a CreateQuizDTO from string parameters.
     *
     * @param quizType Type of quiz as string
     * @param quizLevel Difficulty level as string
     * @param numberOfQuestions Number of questions to generate
     */
    public CreateQuizDTO(String quizType, String quizLevel, Integer numberOfQuestions) {
        this.quizType = QuizType.valueOf(quizType);
        this.quizLevel = QuizLevel.valueOf(quizLevel);
        this.numberOfQuestions = numberOfQuestions;
    }

    /**
     * Default constructor for deserialization
     */
    public CreateQuizDTO() {
    }
}
