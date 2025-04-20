package com.lpu.mind_maze_ai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for OpenAPI documentation.
 * Sets up Swagger UI and API documentation endpoints.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures OpenAPI documentation settings.
     *
     * @return Configured OpenAPI instance
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mind Maze AI API")
                        .version("1.0")
                        .description("API documentation for the Mind Maze AI application"));
    }
}
