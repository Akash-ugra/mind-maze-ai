package com.lpu.mind_maze_ai.service;

import com.lpu.mind_maze_ai.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * Provides user authentication and lookup functionality by integrating
 * with the application's user repository.
 *
 * This service is responsible for:
 * - Loading user details for authentication
 * - Converting database user entities to Spring Security UserDetails
 * - Handling username lookup failures
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /** Repository for user data access */
    private final UserRepository userRepository;

    /**
     * Creates a new CustomUserDetailsService with required dependencies.
     *
     * @param userRepository Repository for accessing user data
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user's details by username for authentication.
     *
     * Implementation Details:
     * 1. Queries the user repository for the given username
     * 2. Throws exception if user not found
     * 3. Returns user details for Spring Security
     *
     * @param username Username to look up
     * @return UserDetails implementation containing user's security details
     * @throws UsernameNotFoundException if user not found with given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
