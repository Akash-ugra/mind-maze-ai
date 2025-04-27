package com.lpu.mind_maze_ai.service;

import com.lpu.mind_maze_ai.entity.CustomUserDetails;
import com.lpu.mind_maze_ai.entity.Quiz;
import com.lpu.mind_maze_ai.model.CreationStatus;
import com.lpu.mind_maze_ai.model.QuizMapper;
import com.lpu.mind_maze_ai.repository.QuizProgressRepository;
import com.lpu.mind_maze_ai.repository.QuizRepository;
import com.lpu.mind_maze_ai.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.lpu.mind_maze_ai.web.request.dto.CreateQuizDTO;

import java.util.List;
import java.util.UUID;

/**
 * Service implementation for managing quizzes.
 * This class provides methods for creating, retrieving, and deleting quizzes for users.
 */
@Service
public class QuizServiceImpl implements QuizService {
    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

    private final QuizRepository quizRepository;
    private final OllamaQuizService ollamaQuizService;
    private final UserRepository userRepository;
    private final QuizProgressRepository quizProgressRepository;

    /**
     * Constructor for QuizServiceImpl.
     *
     * @param quizRepository Repository for managing Quiz entities.
     * @param ollamaQuizService Service for generating quiz questions.
     * @param userRepository Repository for managing User entities.
     * @param quizProgressRepository Repository for managing quiz progress.
     */
    public QuizServiceImpl(QuizRepository quizRepository, OllamaQuizService ollamaQuizService,
                           UserRepository userRepository, QuizProgressRepository quizProgressRepository) {
        this.quizRepository = quizRepository;
        this.ollamaQuizService = ollamaQuizService;
        this.userRepository = userRepository;
        this.quizProgressRepository = quizProgressRepository;
    }

    /**
     * Retrieves all quizzes for a specific user.
     *
     * @param userId The ID of the user whose quizzes are to be retrieved.
     * @return A list of quizzes associated with the user.
     * @throws RuntimeException if the user is not found.
     */
    @Override
    public List<Quiz> getAllQuizzesByUser(Long userId) {
        logger.debug("Fetching all quizzes for user with ID: {}", userId);
        CustomUserDetails user = getUser(userId);
        List<Quiz> quizzes = quizRepository.findAllByUser(user);
        logger.info("Retrieved {} quizzes for user with ID: {}", quizzes.size(), userId);
        return quizzes;
    }

    /**
     * Retrieves a specific quiz by its ID and the user ID.
     *
     * @param quizId The ID of the quiz to retrieve.
     * @param userId The ID of the user who owns the quiz.
     * @return The quiz entity if found.
     * @throws RuntimeException if the quiz or user is not found.
     */
    @Override
    public Quiz getQuizByIdAndUser(String quizId, Long userId) {
        logger.debug("Fetching quiz with ID: {} for user with ID: {}", quizId, userId);
        CustomUserDetails user = getUser(userId);
        UUID quizUUID = UUID.fromString(quizId);
        Quiz quiz = quizRepository.findByIdAndUser(quizUUID, user)
            .orElseThrow(() -> {
                logger.error("Quiz not found with ID: {} for user with ID: {}", quizId, userId);
                return new RuntimeException("Quiz not found with ID: " + quizId);
            });
        logger.info("Successfully retrieved quiz with ID: {} for user with ID: {}", quizId, userId);
        return quiz;
    }

    /**
     * Deletes a specific quiz for a user.
     *
     * @param quizId The ID of the quiz to delete.
     * @param userId The ID of the user who owns the quiz.
     * @throws RuntimeException if the quiz or user is not found.
     */
    @Override
    @Transactional
    public void deleteQuizByUser(String quizId, Long userId) {
        logger.debug("Deleting quiz with ID: {} for user with ID: {}", quizId, userId);
        CustomUserDetails user = getUser(userId);
        UUID quizUUID = UUID.fromString(quizId);

        Quiz quiz = quizRepository.findByIdAndUser(quizUUID, user)
            .orElseThrow(() -> {
                logger.error("Quiz not found with ID: {} for user with ID: {}", quizId, userId);
                return new RuntimeException("Quiz not found with ID: " + quizId);
            });

        quizProgressRepository.deleteByQuiz(quiz);
        logger.info("Deleted quiz progress for quiz with ID: {}", quizId);

        quizRepository.delete(quiz);
        logger.info("Deleted quiz with ID: {} for user with ID: {}", quizId, userId);
    }

    /**
     * Creates a new quiz for a user.
     *
     * @param createQuizDTO The data transfer object containing quiz details.
     * @param userId The ID of the user for whom the quiz is being created.
     * @return The created quiz entity.
     */
    @Override
    public Quiz createQuizForUser(CreateQuizDTO createQuizDTO, Long userId) {
        logger.debug("Creating quiz for user with ID: {}", userId);
        CustomUserDetails user = getUser(userId);

        Quiz quiz = QuizMapper.toEntity(createQuizDTO);
        quiz.setUser(user);
        quiz.setCreationStatus(CreationStatus.NOT_STARTED.name());

        Quiz savedQuiz = quizRepository.save(quiz);
        logger.info("Quiz created with ID: {} for user with ID: {}", savedQuiz.getId(), userId);

        ollamaQuizService.getQuizQuestions(savedQuiz);
        logger.info("Quiz questions generated for quiz with ID: {}", savedQuiz.getId());

        return savedQuiz;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The user entity if found.
     * @throws RuntimeException if the user is not found.
     */
    private CustomUserDetails getUser(Long userId) {
        logger.debug("Fetching user with ID: {}", userId);
        return userRepository.findById(userId)
            .orElseThrow(() -> {
                logger.error("User not found with ID: {}", userId);
                return new RuntimeException("User not found with ID: " + userId);
            });
    }
}
