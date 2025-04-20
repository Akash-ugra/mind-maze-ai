package com.lpu.mind_maze_ai.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

/**
 * Entity representing a question within a quiz.
 * Contains:
 * - Question text
 * - Answer options
 * - Correct answer
 */
@Entity
@Getter
@Setter
public class QuizQuestion extends Auditable {
    /** Unique identifier for the question */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Quiz this question belongs to */
    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    /** The question text */
    private String question;

    /** List of possible answer options */
    @ElementCollection
    @CollectionTable(name = "quiz_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_value")
    private List<String> ansOptions;

    /** The correct answer option */
    private String correctAnswer;
}
