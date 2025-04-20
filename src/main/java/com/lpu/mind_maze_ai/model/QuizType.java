package com.lpu.mind_maze_ai.model;

/**
 * Enumeration of available quiz types in the system.
 * Each type represents a different subject area for quizzes.
 */
public enum QuizType {
    /** Mathematics related quizzes */
    MATH("Math"),
    /** Science related quizzes */
    SCIENCE("Science"),
    /** History related quizzes */
    HISTORY("History"),
    /** Geography related quizzes */
    GEOGRAPHY("Geography"),
    /** Literature related quizzes */
    LITERATURE("Literature"),
    /** Art related quizzes */
    ART("Art"),
    /** Music related quizzes */
    MUSIC("Music"),
    /** Technology related quizzes */
    TECHNOLOGY("Technology"),
    /** Sports related quizzes */
    SPORTS("Sports"),
    /** General Knowledge related quizzes */
    GENERAL_KNOWLEDGE("General Knowledge");

    private final String type;

    /**
     * Constructs a quiz type with its display name.
     *
     * @param type Display name of the quiz type
     */
    QuizType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
