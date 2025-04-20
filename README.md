# Mind Maze AI - Server

A Spring Boot backend service for Mind Maze AI, an adaptive quiz platform powered by AI.

## Prerequisites

- Java 21 or higher
- Maven 3.9+
- Docker & Docker Compose
- PostgreSQL 15+
- Ollama (for AI model serving)

## Development Setup

1. Clone the repository:
```bash
git clone https://github.com/yourusername/mind-maze-ai.git
cd mind-maze-ai
```

2. Start required services using Docker Compose:
```bash
cd docker
docker-compose up -d
```

This will start:
- PostgreSQL database on port 5432
- Ollama AI server on port 11434 with deepseek-r1:7b model

3. Configure application:
```bash
cp src/main/resources/application.yaml src/main/resources/application-local.yaml
```

Edit `application-local.yaml` with your local settings if needed.

4. Build the project:
```bash
./mvnw clean install
```

5. Run the application:
```bash
./mvnw spring-boot:run
```

The server will start at `http://localhost:8080`

### API Documentation

Once running, you can access:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Spec: `http://localhost:8080/v3/api-docs`

## Project Structure

```
mind-maze-ai/
├── src/
│   ├── main/
│   │   ├── java/         # Java source files
│   │   └── resources/    # Configuration files
│   └── test/             # Test files
├── docker/               # Docker configurations
│   └── docker-compose.yml
└── pom.xml              # Maven configuration
```

### Key Features

- JWT-based authentication
- AI-powered quiz generation
- Async quiz processing
- RESTful API
- PostgreSQL persistence
- OpenAPI documentation

## Production Deployment

### Using Docker

1. Build the Docker image:
```bash
docker build -t mind-maze-ai .
```

2. Configure environment variables:
```bash
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host:5432/MINDMAZE
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password
export SPRING_AI_OLLAMA_BASE_URL=http://your-ollama-host:11434
```

3. Run the container:
```bash
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE \
  -e SPRING_DATASOURCE_URL \
  -e SPRING_DATASOURCE_USERNAME \
  -e SPRING_DATASOURCE_PASSWORD \
  -e SPRING_AI_OLLAMA_BASE_URL \
  mind-maze-ai
```

### Manual Deployment

1. Build the JAR:
```bash
./mvnw clean package -DskipTests
```

2. Copy the JAR and configure the environment:
```bash
scp target/mind-maze-ai-*.jar your-server:/opt/mind-maze/
```

3. Create a systemd service:
```ini
[Unit]
Description=Mind Maze AI Server
After=network.target

[Service]
User=mindmaze
Environment=SPRING_PROFILES_ACTIVE=prod
Environment=SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/MINDMAZE
WorkingDirectory=/opt/mind-maze
ExecStart=java -jar mind-maze-ai.jar
Restart=always

[Install]
WantedBy=multi-user.target
```

## API Authentication

All API endpoints (except `/api/v1/auth/login` and `/api/v1/auth/register`) require JWT authentication.

Include the JWT token in request headers:
```
Authorization: Bearer <your-jwt-token>
```
