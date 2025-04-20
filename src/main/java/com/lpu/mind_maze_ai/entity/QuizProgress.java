package com.lpu.mind_maze_ai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Entity representing a user's progress in a specific quiz.
 * Tracks:
 * - Questions asked
 * - Current question
 * - Score
 * - Completion status
 */
@Entity
@Getter
@Setter
public class QuizProgress extends Auditable {
    /** Unique identifier for the progress entry */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** User taking the quiz */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUserDetails user;

    /** Quiz being attempted */
    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    /** Set of question IDs that have been asked */
    @ElementCollection
    private Set<UUID> askedQuestionIds = new HashSet<>();

    /** ID of the current question being attempted */
    private UUID currentQuestionId;

    /** Number of correct answers */
    private int score;

    /** Total number of questions in the quiz */
    private int totalQuestions;

    /** Number of wrong answers */
    private int wrongAnswers;

    /** Whether the quiz has been completed */
    private boolean completed;
}
