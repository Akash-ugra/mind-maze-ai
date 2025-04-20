package com.lpu.mind_maze_ai.service;

import com.lpu.mind_maze_ai.entity.Quiz;

/**
 * Service interface for generating quiz questions using Ollama AI.
 */
public interface OllamaQuizService {
    /**
     * Generates quiz questions using AI for a given quiz.
     *
     * @param quiz The quiz entity for which questions need to be generated
     * @throws com.lpu.mind_maze_ai.exception.QuizCreationException if there's an error during question generation
     */
    void getQuizQuestions(Quiz quiz);
}
