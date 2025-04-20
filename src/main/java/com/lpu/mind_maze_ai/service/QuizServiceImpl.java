package com.lpu.mind_maze_ai.service;

import com.lpu.mind_maze_ai.entity.CustomUserDetails;
import com.lpu.mind_maze_ai.entity.Quiz;
import com.lpu.mind_maze_ai.model.CreationStatus;
import com.lpu.mind_maze_ai.model.QuizMapper;
import com.lpu.mind_maze_ai.repository.QuizProgressRepository;
import com.lpu.mind_maze_ai.repository.QuizRepository;
import com.lpu.mind_maze_ai.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.lpu.mind_maze_ai.web.request.dto.CreateQuizDTO;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of the QuizService interface that manages quiz operations.
 * This service handles quiz creation, retrieval, and deletion for users.
 */
@Service
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final OllamaQuizService ollamaQuizService;
    private final UserRepository userRepository;
    private final QuizProgressRepository quizProgressRepository;

    /**
     * Creates a new QuizServiceImpl with required dependencies.
     *
     * @param quizRepository Repository for managing quiz data
     * @param ollamaQuizService Service for generating quiz questions using AI
     * @param userRepository Repository for managing user data
     * @param quizProgressRepository Repository for managing quiz progress
     */
    public QuizServiceImpl(QuizRepository quizRepository, OllamaQuizService ollamaQuizService,
                           UserRepository userRepository, QuizProgressRepository quizProgressRepository) {
        this.quizRepository = quizRepository;
        this.ollamaQuizService = ollamaQuizService;
        this.userRepository = userRepository;
        this.quizProgressRepository = quizProgressRepository;
    }

    /**
     * Retrieves all quizzes belonging to a specific user.
     *
     * Implementation Details:
     * 1. Validates user exists
     * 2. Fetches all quizzes associated with the user
     *
     * @param userId Identifier of the user whose quizzes to retrieve
     * @return List of quizzes owned by the user
     * @throws RuntimeException if user not found
     */
    @Override
    public List<Quiz> getAllQuizzesByUser(Long userId) {
        // Fetch the user by ID
        CustomUserDetails user = getUser(userId);
        return quizRepository.findAllByUser(user);
    }

    @Override
    public Quiz getQuizByIdAndUser(String quizId, Long userId) {
        // Fetch the user by ID
        CustomUserDetails user = getUser(userId);
        // Convert the quizId string to a UUID
        UUID quizUUID = UUID.fromString(quizId);
        return quizRepository.findByIdAndUser(quizUUID, user)
                .orElseThrow(() -> new RuntimeException("Quiz not found with ID: " + quizId));
    }

    @Override
    @Transactional
    public void deleteQuizByUser(String quizId, Long userId) {
        // Fetch the user by ID
        CustomUserDetails user = getUser(userId);
        // Convert the quizId string to a UUID
        UUID quizUUID = UUID.fromString(quizId);

        // Check if the quiz exists for the user
        Quiz quiz = quizRepository.findByIdAndUser(quizUUID, user)
                .orElseThrow(() -> new RuntimeException("Quiz not found with ID: " + quizId));

        // Delete related quiz progress
        quizProgressRepository.deleteByQuiz(quiz);

        // Delete the quiz
        quizRepository.delete(quiz);
    }

    @Override
    public Quiz createQuizForUser(CreateQuizDTO createQuizDTO, Long userId) {
        CustomUserDetails user = getUser(userId);
        // Convert DTO to entity
        Quiz quiz = QuizMapper.toEntity(createQuizDTO);
        quiz.setUser(user);
        quiz.setCreationStatus(CreationStatus.NOT_STARTED.name());

        // Save the quiz to the database and return the saved entity
        Quiz savedQuiz = quizRepository.save(quiz);
        ollamaQuizService.getQuizQuestions(savedQuiz);
        return savedQuiz;
    }

    private CustomUserDetails getUser(Long userId) {
        // Fetch the user by ID
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
}
