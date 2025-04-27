package com.lpu.mind_maze_ai.service;

import com.lpu.mind_maze_ai.entity.QuizProgress;
import com.lpu.mind_maze_ai.entity.QuizQuestion;
import com.lpu.mind_maze_ai.exception.QuestionAlreadyAskedException;
import com.lpu.mind_maze_ai.repository.QuizProgressRepository;
import com.lpu.mind_maze_ai.repository.QuizQuestionRepo;
import com.lpu.mind_maze_ai.repository.UserRepository;
import com.lpu.mind_maze_ai.web.response.dto.CorrectResponseDTO;
import com.lpu.mind_maze_ai.web.response.dto.QuestionDTO;
import com.lpu.mind_maze_ai.web.response.dto.ScoreDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of QuizProgressService that manages quiz progress tracking.
 * This service handles:
 * - Question selection and delivery
 * - Progress tracking
 * - Score calculation
 * - Answer validation
 *
 * The service ensures questions are not repeated within a quiz session and
 * maintains the user's progress throughout the quiz attempt.
 *
 * @see QuizProgressService
 * @see QuizProgress
 * @see QuizQuestion
 */
@Service
public class QuizProgressServiceImpl implements QuizProgressService {
    private static final Logger logger = LoggerFactory.getLogger(QuizProgressServiceImpl.class);
    private final QuizProgressRepository progressRepository;
    private final QuizQuestionRepo questionRepo;
    private final UserRepository userRepository;

    /**
     * Creates a new instance of QuizProgressServiceImpl with required dependencies.
     *
     * @param progressRepository Repository for managing quiz progress data
     * @param questionRepo Repository for managing quiz questions
     * @param userRepository Repository for managing user data
     */
    public QuizProgressServiceImpl(QuizProgressRepository progressRepository, QuizQuestionRepo questionRepo, UserRepository userRepository) {
        this.progressRepository = progressRepository;
        this.questionRepo = questionRepo;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a random unasked question from the quiz for the user.
     * If no progress exists for the user and quiz, creates a new progress entry.
     * Tracks which questions have been asked to ensure no repetition.
     *
     * Implementation Details:
     * 1. Fetches all questions for the quiz
     * 2. Creates or retrieves quiz progress for the user
     * 3. Filters out previously asked questions
     * 4. Updates progress with the selected question
     * 5. Returns the question as a DTO
     *
     * @param quizId Unique identifier of the quiz
     * @param userId Identifier of the user taking the quiz
     * @return DTO containing the selected question and its options
     * @throws QuestionAlreadyAskedException if all questions have been asked
     */
    @Override
    public QuestionDTO getRandomQuestion(String quizId, Long userId) {
        logger.debug("Getting random question for quiz: {}, user: {}", quizId, userId);
        // Fetch all questions tied to the quiz
        List<QuizQuestion> questions = questionRepo.findAllByQuizId(UUID.fromString(quizId));

        // Fetch the user's progress for the quiz
        QuizProgress progress = progressRepository.findByUserIdAndQuizId(userId, UUID.fromString(quizId))
                .orElseGet(() -> {
                    QuizProgress newProgress = new QuizProgress();
                    newProgress.setUser(userRepository.findById(userId).orElseThrow());
                    newProgress.setQuiz(questions.getFirst().getQuiz());
                    newProgress.setScore(0);
                    newProgress.setWrongAnswers(0);
                    newProgress.setCompleted(false);
                    newProgress.setAskedQuestionIds(new HashSet<>()); // Initialize asked questions list
                    newProgress.setTotalQuestions(questions.size());
                    return progressRepository.save(newProgress);
                });

        // Find the next question that hasn't been asked yet
        Optional<QuizQuestion> nextQuestion = questions.stream()
                .filter(question -> !progress.getAskedQuestionIds().contains(question.getId())) // Exclude asked questions
                .findFirst();

        if (nextQuestion.isEmpty()) {
            logger.info("No more questions available for quiz: {}, user: {}", quizId, userId);
            throw new QuestionAlreadyAskedException(String.format("[userId: %s, quizId: %s]No more questions available for this quiz", userId, quizId));
        }

        // Update progress with the current question
        QuizQuestion question = nextQuestion.get();
        progress.getAskedQuestionIds().add(question.getId()); // Add to asked questions list
        progress.setCurrentQuestionId(question.getId());
        progressRepository.save(progress);

        // Map the question to a DTO
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setQuestion(question.getQuestion());
        questionDTO.setOptions(question.getAnsOptions());
        logger.debug("Retrieved random question for quiz: {}, user: {}", quizId, userId);
        return questionDTO;
    }

    /**
     * Records the user's answer to a question and updates the quiz progress.
     * Calculates whether the answer was correct and updates score accordingly.
     *
     * Implementation Details:
     * 1. Retrieves the current quiz progress
     * 2. Validates the answer against the correct answer
     * 3. Updates score or wrong answer count
     * 4. Saves the updated progress
     * 5. Returns feedback about the answer
     *
     * @param quizId Unique identifier of the quiz
     * @param userId Identifier of the user taking the quiz
     * @param questionId Identifier of the question being answered
     * @param selectedOption The option selected by the user
     * @return DTO containing feedback about the answer correctness
     * @throws RuntimeException if progress or question cannot be found
     */
    @Override
    public CorrectResponseDTO saveProgress(String quizId, Long userId, String questionId, String selectedOption) {
        logger.debug("Saving progress for quiz: {}, user: {}, question: {}", quizId, userId, questionId);
        QuizProgress progress = progressRepository.findByUserIdAndQuizId(userId, UUID.fromString(quizId))
                .orElseThrow(() -> new RuntimeException("Progress not found for user and quiz."));

        // Check if the answer is correct
        QuizQuestion question = questionRepo.findById(UUID.fromString(questionId))
                .orElseThrow(() -> new RuntimeException("Question not found."));
        if (question.getCorrectAnswer().equalsIgnoreCase(selectedOption)) {
            progress.setScore(progress.getScore() + 1);
        } else {
            progress.setWrongAnswers(progress.getWrongAnswers() + 1);
        }
        progress.setCurrentQuestionId(UUID.fromString(questionId));
        progressRepository.save(progress);
        CorrectResponseDTO responseDTO = new CorrectResponseDTO();
        responseDTO.setIsCorrect(question.getCorrectAnswer().equalsIgnoreCase(selectedOption));
        responseDTO.setCorrectOption(question.getCorrectAnswer());
        responseDTO.setQuestion(question.getQuestion());
        logger.info("Progress saved for quiz: {}, user: {}, question: {}, correct: {}",
            quizId, userId, questionId, question.getCorrectAnswer().equalsIgnoreCase(selectedOption));
        return responseDTO;
    }

    /**
     * Resumes a quiz in progress by returning the last question that was being answered.
     * Used when a user returns to a quiz they haven't completed.
     *
     * Implementation Details:
     * 1. Retrieves the quiz progress
     * 2. Finds the last question that was being answered
     * 3. Returns the question as a DTO if found
     *
     * @param quizId Unique identifier of the quiz
     * @param userId Identifier of the user taking the quiz
     * @return Optional containing the current question, or empty if no question is in progress
     */
    @Override
    public Optional<QuestionDTO> resumeQuiz(String quizId, Long userId) {
        return progressRepository.findByUserIdAndQuizId(userId, UUID.fromString(quizId))
                .map(progress -> {
                    QuizQuestion question = questionRepo.findById(progress.getCurrentQuestionId()).orElseThrow();
                    QuestionDTO questionDTO = new QuestionDTO();
                    questionDTO.setId(question.getId());
                    questionDTO.setQuestion(question.getQuestion());
                    questionDTO.setOptions(question.getAnsOptions());
                    return questionDTO;
                });
    }

    /**
     * Retrieves the current score and progress statistics for a quiz attempt.
     *
     * Implementation Details:
     * 1. Retrieves the quiz progress
     * 2. Compiles score statistics including correct answers, wrong answers, and total questions
     * 3. Returns the statistics as a DTO
     *
     * @param quizId Unique identifier of the quiz
     * @param userId Identifier of the user taking the quiz
     * @return DTO containing score statistics
     * @throws RuntimeException if progress cannot be found
     */
    @Override
    public ScoreDTO getScore(String quizId, Long userId) {
        QuizProgress quizProgress = progressRepository.findByUserIdAndQuizId(userId, UUID.fromString(quizId))
                .orElseThrow(() -> new RuntimeException("Progress not found for user and quiz."));
        ScoreDTO scoreDTO = new ScoreDTO();
        scoreDTO.setCorrectAnswers(quizProgress.getScore());
        scoreDTO.setTotalQuestions(quizProgress.getTotalQuestions());
        scoreDTO.setWrongAnswers(quizProgress.getWrongAnswers());
        return scoreDTO;
    }
}
