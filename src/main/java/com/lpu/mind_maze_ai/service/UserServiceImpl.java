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

/**
 * Implementation of the UserService interface that handles user management operations.
 * This service manages user registration and authentication details.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new UserServiceImpl with required dependencies.
     *
     * @param userRepository Repository for managing user data
     * @param passwordEncoder Encoder for securing user passwords
     */
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user in the system with specified details.
     *
     * Implementation Details:
     * 1. Validates username and email uniqueness
     * 2. Creates new user with encoded password
     * 3. Assigns specified role
     * 4. Saves user and returns DTO
     *
     * @param username Desired username
     * @param password User's password (will be encoded)
     * @param name User's full name
     * @param email User's email address
     * @param role Initial role to assign
     * @return DTO containing created user information
     * @throws ResourceAlreadyExistsException if username or email exists
     */
    public UserDTO registerUser(String username, String password, String name, String email, String role) {
        if (userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent()) {
            throw new ResourceAlreadyExistsException("Username/Email already exists");
        }

        CustomUserDetails user = new CustomUserDetails(new User(username, passwordEncoder.encode(password),
                Set.of(new SimpleGrantedAuthority(role))), name, email);
        user.setName(name);
        user.setEmail(email);

        CustomUserDetails savedUser = userRepository.save(user);

        return new UserDTO(savedUser.getId(), savedUser.getUsername(), savedUser.getName(), savedUser.getEmail(),
                savedUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
    }
}
