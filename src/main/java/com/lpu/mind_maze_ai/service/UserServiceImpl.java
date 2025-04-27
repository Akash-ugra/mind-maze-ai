package com.lpu.mind_maze_ai.service;

import com.lpu.mind_maze_ai.entity.CustomUserDetails;
import com.lpu.mind_maze_ai.exception.ResourceAlreadyExistsException;
import com.lpu.mind_maze_ai.repository.UserRepository;
import com.lpu.mind_maze_ai.web.response.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the UserService interface that handles user management operations.
 * This service manages user registration and authentication details.
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new UserServiceImpl with required dependencies.
     *
     * @param userRepository  Repository for managing user data
     * @param passwordEncoder Encoder for securing user passwords
     */
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user in the system with specified details.
     * <p>
     * Implementation Details:
     * 1. Validates username and email uniqueness
     * 2. Creates new user with encoded password
     * 3. Assigns specified role
     * 4. Saves user and returns DTO
     *
     * @param username Desired username
     * @param password User's password (will be encoded)
     * @param name     User's full name
     * @param email    User's email address
     * @param role     Initial role to assign
     * @return DTO containing created user information
     * @throws ResourceAlreadyExistsException if username or email exists
     */
    public UserDTO registerUser(String username, String password, String name, String email, String role) {
        logger.debug("Attempting to register user with username: {}, email: {}", username, email);

        if (userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent()) {
            logger.error("Registration failed: Username or email already exists. Username: {}, Email: {}", username, email);
            throw new ResourceAlreadyExistsException("Username/Email already exists");
        }

        logger.debug("Encoding password for user: {}", username);
        CustomUserDetails user = new CustomUserDetails(new User(username, passwordEncoder.encode(password),
            Set.of(new SimpleGrantedAuthority(role))), name, email);
        user.setName(name);
        user.setEmail(email);

        logger.debug("Saving user to the database: {}", username);
        CustomUserDetails savedUser = userRepository.save(user);

        logger.info("User successfully registered with ID: {}, Username: {}", savedUser.getId(), savedUser.getUsername());
        return new UserDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getName(), savedUser.getEmail(),
            savedUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
    }
}
