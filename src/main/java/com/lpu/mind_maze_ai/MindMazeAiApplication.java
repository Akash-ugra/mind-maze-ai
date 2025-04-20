package com.lpu.mind_maze_ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main entry point for the Mind Maze AI application.
 * Enables asynchronous processing for quiz generation.
 */
@EnableAsync
@SpringBootApplication
public class MindMazeAiApplication {
    /**
     * Application startup method.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(MindMazeAiApplication.class, args);
    }
}
