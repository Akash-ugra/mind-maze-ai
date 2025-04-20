package com.lpu.mind_maze_ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpu.mind_maze_ai.entity.Quiz;
import com.lpu.mind_maze_ai.entity.QuizQuestion;
import com.lpu.mind_maze_ai.exception.QuizCreationException;
import com.lpu.mind_maze_ai.model.CreationStatus;
import com.lpu.mind_maze_ai.model.QuizResponse;
import com.lpu.mind_maze_ai.repository.QuizQuestionRepo;
import com.lpu.mind_maze_ai.repository.QuizRepository;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of OllamaQuizService that generates quiz questions using the Ollama AI model.
 * This service handles the asynchronous generation of quiz questions by interacting with an AI model.
 * It processes the AI responses and converts them into quiz questions that can be stored in the database.
 *
 * The service uses a template-based prompt system to generate structured quiz content
 * and ensures the responses conform to the expected JSON format.
 *
 * @see OllamaQuizService
 * @see Quiz
 * @see QuizQuestion
 */
@Service
public class OllamaQuizQuizServiceImpl implements OllamaQuizService {
    private final OllamaChatModel chatModel;
    private final QuizQuestionRepo quizQuestionRepo;
    private final QuizRepository quizRepository;
    private final ObjectMapper objectMapper;
    public static final String promptString = """
            You are a quiz organizer. Your task is to generate a quiz based strictly on the provided details:
            - quizId: {quizId} (a unique identifier, do not modify)
            - quizType: {quizType} (e.g., Math, Science, History, as provided in the input)
            - quizLevel: {quizLevel} (e.g., Easy, Medium, Hard, as provided in the input)
            - numberOfQuestions: {numberOfQuestions} (an integer, as provided in the input)

            Constraints:
            1. Do not modify or infer any values for quizId, quizType, quizLevel, or numberOfQuestions.
            2. Generate exactly the number of questions specified in numberOfQuestions.
            3. Each question must have four options, and only one correct answer.
            4. Ensure the response strictly adheres to the format below.

            Expected Response Format:
            {
                "quizId": "{quizId}",
                "quizType": "{quizType}",
                "quizLevel": "{quizLevel}",
                "numberOfQuestions": {numberOfQuestions},
                "quizQuestionList": [
                    {
                        "question": "question-text",
                        "options": ["option1", "option2", "option3", "option4"],
                        "answer": "correct-answer"
                    }
                ]
            }

            Do not include any additional information or deviate from the format. Only return the JSON response.
            """;

    /**
     * Creates a new instance with required dependencies.
     *
     * @param chatModel The Ollama chat model for generating questions
     * @param objectMapper Mapper for JSON processing
     * @param quizQuestionRepo Repository for quiz questions
     * @param quizRepository Repository for quizzes
     */
    public OllamaQuizQuizServiceImpl(OllamaChatModel chatModel, ObjectMapper objectMapper, QuizQuestionRepo quizQuestionRepo, QuizRepository quizRepository) {
        this.chatModel = chatModel;
        this.objectMapper = objectMapper;
        this.quizQuestionRepo = quizQuestionRepo;
        this.quizRepository = quizRepository;
    }

    /**
     * Asynchronously generates quiz questions using AI.
     *
     * Implementation Details:
     * 1. Creates AI prompt with quiz parameters
     * 2. Updates quiz status to IN_PROGRESS
     * 3. Calls AI model to generate questions
     * 4. Processes response and saves questions
     * 5. Updates quiz status on completion
     *
     * @param quiz The quiz for which to generate questions
     * @throws QuizCreationException if generation fails
     */
    @Override
    @Async
    public void getQuizQuestions(Quiz quiz) {
        // Create a prompt with the provided quiz details
        String formattedPrompt = promptString
                .replace("{quizId}", String.valueOf(quiz.getId()))
                .replace("{quizType}", quiz.getQuizType())
                .replace("{quizLevel}", quiz.getQuizLevel())
                .replace("{numberOfQuestions}", quiz.getNumberOfQuestions().toString());
        Prompt prompt = new Prompt(formattedPrompt);
        quiz.setCreationStatus(CreationStatus.IN_PROGRESS.name());
        quizRepository.saveAndFlush(quiz);
        try {
            ChatResponse chatResponse = chatModel.call(prompt);
            // Parse the response and extract quiz questions
            String text = chatResponse.getResult().getOutput().getText();
            processLLMMessage(text, quiz);
        } catch (Exception e) {
            quiz.setCreationStatus(CreationStatus.FAILURE.name());
            quizRepository.saveAndFlush(quiz);
            throw new QuizCreationException(e.getMessage());
        }
    }

    /**
     * Processes the AI model's response and creates quiz questions.
     * This method handles the parsing of JSON responses and creation of QuizQuestion entities.
     *
     * @param message Raw message response from the AI model
     * @param quiz Quiz entity to associate the generated questions with
     * @throws JsonProcessingException if JSON parsing fails
     * @throws QuizCreationException if the required number of questions is not generated
     */
    private void processLLMMessage(String message, Quiz quiz) throws JsonProcessingException {
        // Remove the <think> section if it exists
        if (message.contains("<think>") && message.contains("</think>")) {
            message = message.replaceAll("<think>.*?</think>", "").trim();
        }

        // Extract JSON object from the cleaned message
        String json = message.substring(message.indexOf("{"), message.lastIndexOf("}") + 1);

        // Parse JSON into QuizResponse
        QuizResponse quizResponse = objectMapper.readValue(json, QuizResponse.class);

        // Save QuizQuestion entities
        List<QuizQuestion> quizQuestions = quizResponse.getQuizQuestionList().stream().map(q -> {
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setQuiz(quiz); // Associate the saved Quiz
            quizQuestion.setQuestion(q.getQuestion());
            quizQuestion.setAnsOptions(q.getOptions());
            quizQuestion.setCorrectAnswer(q.getAnswer());
            return quizQuestion;
        }).toList();

        if (quizQuestions.isEmpty() || quizQuestions.size() != quiz.getNumberOfQuestions()) {
            quiz.setCreationStatus(CreationStatus.FAILURE.name());
            quizRepository.saveAndFlush(quiz);
            throw new QuizCreationException("No questions generated");
        }
        // Save the quiz questions to the database
        List<QuizQuestion> savedQuizQuestions = quizQuestionRepo.saveAll(quizQuestions);
        quiz.setQuizQuestionList(savedQuizQuestions);
        quiz.setCreationStatus(CreationStatus.SUCCESS.name());
        quizRepository.save(quiz);
    }
}
