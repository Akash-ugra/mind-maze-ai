package com.lpu.mind_maze_ai.controller;

import com.lpu.mind_maze_ai.entity.CustomUserDetails;
import com.lpu.mind_maze_ai.exception.ResourceAlreadyExistsException;
import com.lpu.mind_maze_ai.service.UserService;
import com.lpu.mind_maze_ai.util.JwtUtil;
import com.lpu.mind_maze_ai.web.request.dto.CreateUserDTO;
import com.lpu.mind_maze_ai.web.request.dto.LoginDTO;
import com.lpu.mind_maze_ai.web.response.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST Controller for handling authentication operations.
 * Provides endpoints for user registration and login.
 * Manages JWT token generation and user authentication.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param loginDTO Contains user login credentials
     * @return Map containing the JWT token
     * @throws org.springframework.security.core.AuthenticationException if authentication fails
     */
    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginDTO loginDTO) {
        logger.info("Login attempt for user: {}", loginDTO.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(authentication.getName(), userDetails.getId(),
                userDetails.getName(), userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).toList());

            logger.info("Successful login for user: {}", loginDTO.getUsername());
            return Map.of("token", token);
        } catch (Exception e) {
            logger.error("Failed login attempt for user: {}", loginDTO.getUsername(), e);
            throw e;
        }
    }

    /**
     * Registers a new user in the system.
     *
     * @param createUserDTO Contains user registration details
     * @return ResponseEntity containing the created user's details
     * @throws ResourceAlreadyExistsException if username/email already exists
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        logger.info("Registration attempt for username: {}", createUserDTO.getUsername());
        try {
            UserDTO userDTO = userService.registerUser(createUserDTO.getUsername(),
                createUserDTO.getPassword(), createUserDTO.getName(),
                createUserDTO.getEmail(), "ROLE_USER");
            logger.info("Successfully registered user: {}", createUserDTO.getUsername());
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            logger.error("Failed to register user: {}", createUserDTO.getUsername(), e);
            throw e;
        }
    }
}
