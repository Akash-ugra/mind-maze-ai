package com.lpu.mind_maze_ai.repository;

import com.lpu.mind_maze_ai.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing QuizQuestion entities.
 * Handles data access operations for quiz questions.
 */
public interface QuizQuestionRepo extends JpaRepository<QuizQuestion, UUID> {
    /**
     * Retrieves all questions belonging to a specific quiz.
     *
     * @param quizId ID of the quiz
     * @return List of questions for the specified quiz
     */
    List<QuizQuestion> findAllByQuizId(UUID quizId);
}
