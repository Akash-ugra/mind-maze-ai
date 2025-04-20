package com.lpu.mind_maze_ai.model;

/**
 * Enumeration representing the various states of quiz creation.
 * Tracks the lifecycle of quiz generation from initiation to completion.
 */
public enum CreationStatus {
    /** Quiz generation completed successfully */
    SUCCESS("Success"),
    /** Quiz generation failed */
    FAILURE("Failure"),
    /** Quiz generation is currently in progress */
    IN_PROGRESS("In Progress"),
    /** Quiz generation has not yet started */
    NOT_STARTED("Not Started");

    private final String status;

    CreationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
