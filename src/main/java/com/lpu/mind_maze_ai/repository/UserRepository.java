package com.lpu.mind_maze_ai.repository;

import com.lpu.mind_maze_ai.entity.CustomUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing CustomUserDetails entities.
 * Handles data access operations for user accounts.
 */
public interface UserRepository extends JpaRepository<CustomUserDetails, Long> {
    /**
     * Finds a user by their username.
     * Used for authentication and username uniqueness validation.
     *
     * @param username Username to search for
     * @return Optional containing the user if found
     */
    Optional<CustomUserDetails> findByUsername(String username);

    /**
     * Finds a user by their email address.
     * Used for email uniqueness validation.
     *
     * @param email Email address to search for
     * @return Optional containing the user if found
     */
    Optional<CustomUserDetails> findByEmail(String email);
}
