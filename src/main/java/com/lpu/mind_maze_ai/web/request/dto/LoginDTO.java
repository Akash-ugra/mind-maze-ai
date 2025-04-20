package com.lpu.mind_maze_ai.web.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for login requests.
 * Contains credentials required for user authentication.
 */
@Data
public class LoginDTO {
    /** Username for authentication */
    @NotBlank
    private String username;

    /** User's password */
    @NotBlank
    private String password;
}
