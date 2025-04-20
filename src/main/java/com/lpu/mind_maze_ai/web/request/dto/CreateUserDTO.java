package com.lpu.mind_maze_ai.web.request.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for user registration requests.
 * Contains required information for creating a new user account.
 */
@Data
public class CreateUserDTO {
    /** Username for authentication - must be unique */
    @NotBlank
    String username;

    /** Password for authentication - stored encrypted */
    @NotBlank
    String password;

    /** User's full name */
    @NotBlank
    String name;

    /** User's email address - must be unique */
    @NotBlank
    @Email
    String email;
}
