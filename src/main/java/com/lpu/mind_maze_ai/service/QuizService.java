package com.lpu.mind_maze_ai.service;

import com.lpu.mind_maze_ai.entity.Quiz;
import com.lpu.mind_maze_ai.web.request.dto.CreateQuizDTO;

import java.util.List;

/**
 * Service interface for managing quiz operations.
 */
public interface QuizService {
    /**
     * Retrieves all quizzes associated with a specific user.
     *
     * @param userId The ID of the user whose quizzes are to be retrieved
     * @return List of quizzes belonging to the user
     */
    List<Quiz> getAllQuizzesByUser(Long userId);

    /**
     * Retrieves a specific quiz for a user.
     *
     * @param quizId The ID of the quiz to retrieve
     * @param userId The ID of the user who owns the quiz
     * @return The requested quiz entity
     * @throws RuntimeException if the quiz is not found
     */
    Quiz getQuizByIdAndUser(String quizId, Long userId);

    /**
     * Deletes a specific quiz associated with a user.
     *
     * @param quizId The ID of the quiz to delete
     * @param userId The ID of the user who owns the quiz
     * @throws RuntimeException if the quiz is not found
     */
    void deleteQuizByUser(String quizId, Long userId);

    /**
     * Creates a new quiz for a specific user.
     *
     * @param createQuizDTO DTO containing quiz creation details
     * @param userId The ID of the user for whom the quiz is being created
     * @return The newly created quiz entity
     */
    Quiz createQuizForUser(CreateQuizDTO createQuizDTO, Long userId);
}
