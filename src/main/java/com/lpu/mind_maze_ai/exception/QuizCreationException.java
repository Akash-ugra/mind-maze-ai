package com.lpu.mind_maze_ai.exception;

/**
 * Exception thrown when quiz creation fails.
 * Typically occurs during AI-based question generation process.
 */
public class QuizCreationException extends RuntimeException {
    /**
     * Constructs exception with error message.
     *
     * @param message Description of what went wrong during quiz creation
     */
    public QuizCreationException(String message) {
        super(message);
    }

    public QuizCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
