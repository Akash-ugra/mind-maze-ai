package com.lpu.mind_maze_ai.repository;

import com.lpu.mind_maze_ai.entity.CustomUserDetails;
import com.lpu.mind_maze_ai.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Quiz entities.
 * Provides data access operations for quizzes.
 */
public interface QuizRepository extends JpaRepository<Quiz, UUID> {
    /**
     * Finds all quizzes owned by a specific user.
     *
     * @param user The user whose quizzes to retrieve
     * @return List of quizzes belonging to the user
     */
    List<Quiz> findAllByUser(CustomUserDetails user);

    /**
     * Finds a specific quiz owned by a specific user.
     *
     * @param quizId ID of the quiz to find
     * @param user Owner of the quiz
     * @return Optional containing the quiz if found
     */
    Optional<Quiz> findByIdAndUser(UUID quizId, CustomUserDetails user);
}
