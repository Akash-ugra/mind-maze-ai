package com.lpu.mind_maze_ai.model;

import com.lpu.mind_maze_ai.entity.CustomUserDetails;
import com.lpu.mind_maze_ai.web.response.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Collectors;

/**
 * Utility class for mapping between User entities and DTOs.
 * Handles conversion of user-related objects.
 */
public class UserMapper {
    /**
     * Converts a CustomUserDetails entity to a UserDTO.
     * Extracts relevant user information for client-side use.
     *
     * @param user User entity to convert
     * @return UserDTO containing user details
     */
    public static UserDTO toUserDto(CustomUserDetails user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getName(),
                user.getEmail(), user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
    }
}
