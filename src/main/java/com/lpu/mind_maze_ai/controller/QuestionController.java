package com.lpu.mind_maze_ai.controller;

import com.lpu.mind_maze_ai.exception.QuestionAlreadyAskedException;
import com.lpu.mind_maze_ai.service.QuizProgressService;
import com.lpu.mind_maze_ai.web.response.dto.CorrectResponseDTO;
import com.lpu.mind_maze_ai.web.response.dto.QuestionDTO;
import com.lpu.mind_maze_ai.web.response.dto.ScoreDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST Controller for managing quiz question operations.
 * Handles question delivery, progress tracking, and score management.
 * Provides endpoints for:
 * - Getting random questions
 * - Saving answer progress
 * - Resuming quizzes
 * - Retrieving scores
 */
@RestController
@RequestMapping("/api/v1/question")
public class QuestionController {
    private final QuizProgressService quizProgressService;

    public QuestionController(QuizProgressService quizProgressService) {
        this.quizProgressService = quizProgressService;
    }

    /**
     * Retrieves a random question for a specific quiz.
     *
     * @param quizId ID of the quiz
     * @param userId ID of the user taking the quiz
     * @return ResponseEntity containing the question details
     * @throws QuestionAlreadyAskedException if no more questions are available
     */
    @GetMapping("/random")
    public ResponseEntity<QuestionDTO> getRandomQuestion(@RequestParam String quizId, @RequestParam Long userId) {
        QuestionDTO question = quizProgressService.getRandomQuestion(quizId, userId);
        return ResponseEntity.ok(question);
    }

    /**
     * Saves user's answer and returns feedback.
     *
     * @param quizId ID of the quiz
     * @param userId ID of the user
     * @param questionId ID of the question being answered
     * @param selectedOption User's selected answer
     * @return ResponseEntity containing answer correctness information
     */
    @PostMapping("/saveProgress")
    public ResponseEntity<CorrectResponseDTO> saveProgress(@RequestParam String quizId, @RequestParam Long userId,
                                          @RequestParam String questionId, @RequestParam String selectedOption) {
        CorrectResponseDTO correctResponseDTO = quizProgressService.saveProgress(quizId, userId, questionId, selectedOption);
        return ResponseEntity.ok().body(correctResponseDTO);
    }

    @GetMapping("/resume")
    public ResponseEntity<QuestionDTO> resumeQuiz(@RequestParam String quizId, @RequestParam Long userId) {
        Optional<QuestionDTO> question = quizProgressService.resumeQuiz(quizId, userId);
        return question.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/score")
    public ResponseEntity<ScoreDTO> getScore(@RequestParam String quizId, @RequestParam Long userId) {
        return ResponseEntity.ok(quizProgressService.getScore(quizId, userId));
    }
}
