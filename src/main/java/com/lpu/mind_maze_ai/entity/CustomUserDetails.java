package com.lpu.mind_maze_ai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Custom implementation of Spring Security's UserDetails.
 * Extends the basic user model with additional fields and implements
 * security-related methods required by Spring Security.
 *
 * This entity represents a user in the system and stores their:
 * - Authentication details (username/password)
 * - Personal information (name/email)
 * - Security authorities (roles)
 * - Associated quizzes
 */
@Entity
@Table(name = "app_user")
@Setter
@Getter
public class CustomUserDetails extends Auditable implements UserDetails {
    /** Unique identifier for the user */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String name; // New field for the user's name
    private String email; // New field for the user's email

    private final Set<GrantedAuthority> authorities;

    @OneToMany(mappedBy = "user")
    private List<Quiz> quizzes;

    /**
     * Creates a new CustomUserDetails from Spring Security User.
     *
     * @param user Spring Security user
     * @param name User's full name
     * @param email User's email address
     */
    public CustomUserDetails(User user, String name, String email) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = new HashSet<>(user.getAuthorities());
    }

    public CustomUserDetails() {
        this.authorities = new HashSet<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
