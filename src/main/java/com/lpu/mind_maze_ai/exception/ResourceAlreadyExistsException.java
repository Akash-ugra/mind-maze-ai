package com.lpu.mind_maze_ai.exception;

/**
 * Exception thrown when attempting to create a resource that already exists.
 * Used primarily for user registration when username or email is already taken.
 */
public class ResourceAlreadyExistsException extends IllegalArgumentException {
    /**
     * Constructs exception with error message.
     *
     * @param message Description of the conflict
     */
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
