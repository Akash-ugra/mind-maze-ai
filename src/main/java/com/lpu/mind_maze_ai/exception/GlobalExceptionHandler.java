package com.lpu.mind_maze_ai.exception;

import com.lpu.mind_maze_ai.web.response.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the application.
 * Provides centralized exception handling across all @RequestMapping methods.
 * Translates exceptions into appropriate HTTP responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles cases where a resource already exists.
     * Returns HTTP 409 CONFLICT with error details.
     *
     * @param ex The exception containing conflict details
     * @return ResponseEntity with error information
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ExceptionDTO(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }

    /**
     * Handles cases where all questions have been asked.
     * Returns HTTP 204 NO_CONTENT with error details.
     *
     * @param ex The exception containing quiz completion details
     * @return ResponseEntity with error information
     */
    @ExceptionHandler(QuestionAlreadyAskedException.class)
    public ResponseEntity<ExceptionDTO> handleQuestionAlreadyAsked(QuestionAlreadyAskedException ex) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ExceptionDTO(ex.getMessage(), HttpStatus.NO_CONTENT.value()));
    }
}
