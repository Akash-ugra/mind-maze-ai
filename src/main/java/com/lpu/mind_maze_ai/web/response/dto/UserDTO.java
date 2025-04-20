package com.lpu.mind_maze_ai.web.response.dto;

import lombok.Data;

import java.util.Set;

/**
 * Data Transfer Object for user information.
 * Contains user details safe for client-side exposure.
 */
@Data
public class UserDTO {
    /** Unique identifier for the user */
    private Long id;
    /** Username for authentication */
    private String username;
    /** User's full name */
    private String name;
    /** User's email address */
    private String email;
    /** Set of roles assigned to the user */
    private Set<String> roles;

    public UserDTO(Long id, String username, String name, String email, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }
}
