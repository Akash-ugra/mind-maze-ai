package com.lpu.mind_maze_ai.web.response.dto;

import lombok.Data;

/**
 * Data Transfer Object for error responses.
 * Used to provide structured error information to clients.
 */
@Data
public class ExceptionDTO {
    /** Error message describing what went wrong */
    private String message;

    /** HTTP status code associated with the error */
    private Integer status;

    /**
     * Creates a new ExceptionDTO with message and status.
     *
     * @param message Error message
     * @param status HTTP status code
     */
    public ExceptionDTO(String message, Integer status) {
        this.message = message;
        this.status = status;
    }
}
