package com.lpu.mind_maze_ai.model;

import com.lpu.mind_maze_ai.entity.Quiz;
import com.lpu.mind_maze_ai.web.request.dto.CreateQuizDTO;
import com.lpu.mind_maze_ai.web.response.dto.QuizDTO;

/**
 * Utility class for mapping between Quiz entities and DTOs.
 * Provides conversion methods for Quiz-related objects.
 */
public class QuizMapper {

    /**
     * Converts a CreateQuizDTO to a Quiz entity.
     *
     * @param createQuizDTO DTO containing quiz creation details
     * @return New Quiz entity instance
     */
    public static Quiz toEntity(CreateQuizDTO createQuizDTO) {
        if (createQuizDTO == null) {
            return null;
        }

        Quiz quiz = new Quiz();
        quiz.setQuizType(createQuizDTO.getQuizType().name());
        quiz.setQuizLevel(createQuizDTO.getQuizLevel().name());
        quiz.setNumberOfQuestions(createQuizDTO.getNumberOfQuestions());
        return quiz;
    }

    /**
     * Converts a Quiz entity to a QuizDTO.
     *
     * @param quiz Quiz entity to convert
     * @return QuizDTO containing quiz details
     */
    public static QuizDTO toDTO(Quiz quiz) {
        if (quiz == null) {
            return null;
        }

        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setQuizId(quiz.getId().toString());
        quizDTO.setQuizType(quiz.getQuizType());
        quizDTO.setQuizLevel(quiz.getQuizLevel());
        quizDTO.setCreationStatus(quiz.getCreationStatus());
        quizDTO.setNumberOfQuestions(quiz.getNumberOfQuestions());
        return quizDTO;
    }
}
