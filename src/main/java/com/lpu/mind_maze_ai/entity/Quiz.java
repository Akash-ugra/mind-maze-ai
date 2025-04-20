package com.lpu.mind_maze_ai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Entity representing a quiz in the system.
 * A quiz consists of multiple questions and is associated with a specific user.
 * It tracks metadata such as quiz type, difficulty level, and creation status.
 *
 * @see QuizQuestion
 * @see CustomUserDetails
 */
@Entity
@Getter
@Setter
public class Quiz extends Auditable {
    /** Unique identifier for the quiz */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Type of quiz (e.g., Math, Science) */
    private String quizType;

    /** Difficulty level of the quiz */
    private String quizLevel;

    /** Number of questions in the quiz */
    private Integer numberOfQuestions;

    /** List of questions associated with this quiz */
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizQuestion> quizQuestionList;

    /** Current status of quiz creation (NOT_STARTED, IN_PROGRESS, SUCCESS, FAILURE) */
    private String creationStatus;

    /** User who created/owns the quiz */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUserDetails user;
}
