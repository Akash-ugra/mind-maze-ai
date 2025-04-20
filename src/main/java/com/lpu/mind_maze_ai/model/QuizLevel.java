package com.lpu.mind_maze_ai.model;

/**
 * Enumeration of difficulty levels for quizzes.
 * Represents increasing levels of complexity from EASY to EXPERT.
 */
public enum QuizLevel {
    /** Basic difficulty level */
    EASY("Easy"),
    /** Intermediate difficulty level */
    MEDIUM("Medium"),
    /** Advanced difficulty level */
    HARD("Hard"),
    /** Expert difficulty level */
    EXPERT("Expert");

    private final String level;

    QuizLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
}
