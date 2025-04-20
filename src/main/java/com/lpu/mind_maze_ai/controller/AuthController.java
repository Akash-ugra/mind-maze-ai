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

/**
 * REST Controller for handling authentication operations.
 * Provides endpoints for user registration and login.
 * Manages JWT token generation and user authentication.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        // Assuming the principal contains user details
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId(); // Replace with your method to get the user ID

        String token = jwtUtil.generateToken(authentication.getName(), userId, userDetails.getName(),
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        return Map.of("token", token);
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
        UserDTO userDTO = userService.registerUser(createUserDTO.getUsername(), createUserDTO.getPassword(),
                createUserDTO.getName(), createUserDTO.getEmail(), "ROLE_USER");
        return ResponseEntity.ok(userDTO);
    }
}
