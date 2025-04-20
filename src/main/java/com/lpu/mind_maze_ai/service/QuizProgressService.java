package com.lpu.mind_maze_ai.service;

import com.lpu.mind_maze_ai.web.response.dto.CorrectResponseDTO;
import com.lpu.mind_maze_ai.web.response.dto.QuestionDTO;
import com.lpu.mind_maze_ai.web.response.dto.ScoreDTO;

import java.util.Optional;

/**
 * Service interface for managing quiz progress and question answering.
 */
public interface QuizProgressService {
    /**
     * Retrieves a random question from the quiz that hasn't been asked yet.
     *
     * @param quizId The ID of the quiz
     * @param userId The ID of the user taking the quiz
     * @return A random question from the quiz
     * @throws com.lpu.mind_maze_ai.exception.QuestionAlreadyAskedException if all questions have been asked
     */
    QuestionDTO getRandomQuestion(String quizId, Long userId);

    /**
     * Saves the user's answer and returns whether it was correct.
     *
     * @param quizId The ID of the quiz
     * @param userId The ID of the user
     * @param questionId The ID of the question being answered
     * @param selectedOption The user's selected answer
     * @return DTO containing information about the correct answer
     */
    CorrectResponseDTO saveProgress(String quizId, Long userId, String questionId, String selectedOption);

    /**
     * Resumes a quiz in progress by returning the last unanswered question.
     *
     * @param quizId The ID of the quiz to resume
     * @param userId The ID of the user
     * @return Optional containing the current question, or empty if quiz is complete
     */
    Optional<QuestionDTO> resumeQuiz(String quizId, Long userId);

    /**
     * Retrieves the current score for a quiz attempt.
     *
     * @param quizId The ID of the quiz
     * @param userId The ID of the user
     * @return DTO containing score information
     */
    ScoreDTO getScore(String quizId, Long userId);
}
