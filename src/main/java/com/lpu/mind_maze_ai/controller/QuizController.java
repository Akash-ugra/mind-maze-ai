package com.lpu.mind_maze_ai.controller;

import com.lpu.mind_maze_ai.entity.Quiz;
import com.lpu.mind_maze_ai.model.QuizMapper;
import com.lpu.mind_maze_ai.service.QuizService;
import com.lpu.mind_maze_ai.web.request.dto.CreateQuizDTO;
import com.lpu.mind_maze_ai.web.response.dto.QuizDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing quiz operations.
 * Provides endpoints for:
 * - Creating new quizzes
 * - Retrieving existing quizzes
 * - Deleting quizzes
 *
 * All operations are user-specific and require authentication.
 */
@RestController
@RequestMapping("/api/v1/quiz")
public class QuizController {
    private final QuizService quizService;

    /**
     * Creates a new QuizController with required dependencies.
     *
     * @param quizService Service handling quiz business logic
     */
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
     * Retrieves all quizzes for a specific user.
     *
     * @param userId ID of the user whose quizzes to retrieve
     * @return ResponseEntity containing list of QuizDTOs
     */
    @GetMapping
    public ResponseEntity<List<QuizDTO>> getAllQuiz(@RequestParam Long userId) {
        // Logic to get all quizzes for a user
        List<Quiz> allQuizzes = quizService.getAllQuizzesByUser(userId);
        List<QuizDTO> quizDTOList = allQuizzes.stream().map(QuizMapper::toDTO).toList();
        return ResponseEntity.ok(quizDTOList);
    }

    /**
     * Retrieves a specific quiz for a user.
     *
     * @param quizId ID of the quiz to retrieve
     * @param userId ID of the user whose quiz to retrieve
     * @return ResponseEntity containing the QuizDTO
     */
    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDTO> getQuiz(@PathVariable String quizId, @RequestParam Long userId) {
        // Logic to get a quiz for a user
        Quiz quizById = quizService.getQuizByIdAndUser(quizId, userId);
        QuizDTO quizDTO = QuizMapper.toDTO(quizById);
        return ResponseEntity.ok(quizDTO);
    }

    /**
     * Deletes a specific quiz for a user.
     *
     * @param quizId ID of the quiz to delete
     * @param userId ID of the user whose quiz to delete
     * @return ResponseEntity indicating the result of the operation
     */
    @DeleteMapping
    public ResponseEntity<?> deleteQuiz(@RequestParam String quizId, @RequestParam Long userId) {
        // Logic to delete a quiz for a user
        quizService.deleteQuizByUser(quizId, userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new quiz for a user.
     *
     * @param createQuizDTO Data Transfer Object containing quiz details
     * @param userId ID of the user creating the quiz
     * @return ResponseEntity containing the created QuizDTO
     */
    @PostMapping("/create")
    public ResponseEntity<QuizDTO> createQuiz(@Valid @RequestBody CreateQuizDTO createQuizDTO, @RequestParam Long userId) {
        // Logic to create a quiz for a user
        Quiz quizCreated = quizService.createQuizForUser(createQuizDTO, userId);
        QuizDTO quizDTO = QuizMapper.toDTO(quizCreated);
        return ResponseEntity.ok(quizDTO);
    }
}
