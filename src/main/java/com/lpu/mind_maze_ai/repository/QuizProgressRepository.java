package com.lpu.mind_maze_ai.repository;

import com.lpu.mind_maze_ai.entity.Quiz;
import com.lpu.mind_maze_ai.entity.QuizProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing QuizProgress entities.
 * Provides data access operations for quiz progress tracking.
 */
public interface QuizProgressRepository extends JpaRepository<QuizProgress, UUID> {
    /**
     * Finds quiz progress for a specific user and quiz combination.
     *
     * @param userId ID of the user
     * @param quizQuizId ID of the quiz
     * @return Optional containing quiz progress if found
     */
    Optional<QuizProgress> findByUserIdAndQuizId(Long userId, UUID quizQuizId);

    /**
     * Deletes all progress records associated with a specific quiz.
     * Used when deleting a quiz to maintain referential integrity.
     *
     * @param quiz Quiz whose progress records should be deleted
     */
    @Modifying
    @Query("DELETE FROM QuizProgress qp WHERE qp.quiz = :quiz")
    void deleteByQuiz(@Param("quiz") Quiz quiz);
}
