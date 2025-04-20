package com.lpu.mind_maze_ai.service;

import com.lpu.mind_maze_ai.web.response.dto.UserDTO;

/**
 * Service interface for managing user operations.
 */
public interface UserService {
    /**
     * Registers a new user in the system.
     *
     * @param username The desired username
     * @param password The user's password
     * @param name The user's full name
     * @param email The user's email address
     * @param role The initial role to assign to the user
     * @return DTO containing the created user's information
     * @throws com.lpu.mind_maze_ai.exception.ResourceAlreadyExistsException if username or email already exists
     */
    UserDTO registerUser(String username, String password, String name, String email, String role);
}
