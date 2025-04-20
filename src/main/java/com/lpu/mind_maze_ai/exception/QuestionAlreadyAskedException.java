package com.lpu.mind_maze_ai.exception;

/**
 * Exception thrown when attempting to get a new question but all questions
 * in the quiz have already been asked.
 */
public class QuestionAlreadyAskedException extends RuntimeException {
    /**
     * Constructs exception with error message.
     *
     * @param message Description identifying the quiz and user
     */
    public QuestionAlreadyAskedException(String message) {
        super(message);
    }

    public QuestionAlreadyAskedException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionAlreadyAskedException(Throwable cause) {
        super(cause);
    }
}
