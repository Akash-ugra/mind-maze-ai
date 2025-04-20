# Deployment Guide

## Prerequisites
- Java 21 or higher
- PostgreSQL 15+
- Docker & Docker Compose 
- Ollama Server with deepseek-r1:7b model

## Environment Setup

### Local Development
```bash
# Start required services
docker-compose -f docker/docker-compose.yml up -d

# Configure environment variables
export SPRING_PROFILES_ACTIVE=local
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/MINDMAZE
export SPRING_DATASOURCE_USERNAME=mind_maze
export SPRING_DATASOURCE_PASSWORD=pass
export SPRING_AI_OLLAMA_BASE_URL=http://localhost:11434
```

### Production Environment
```bash
# Required environment variables
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DATASOURCE_URL=jdbc:postgresql://<db-host>:5432/MINDMAZE
export SPRING_DATASOURCE_USERNAME=<db-user>
export SPRING_DATASOURCE_PASSWORD=<db-password>
export SPRING_AI_OLLAMA_BASE_URL=http://<ollama-host>:11434

# Security settings (recommended for production)
export JWT_SECRET=<your-secure-jwt-key>
export JWT_EXPIRATION=86400000
```

## Build Process

### Development Build
```bash
# Build application
./mvnw clean package -DskipTests

# Run application locally
java -jar target/mind-maze-ai-0.0.1-SNAPSHOT.jar
```

### Production Build
```bash
# Build optimized package
./mvnw clean package -DskipTests -Pprod

# Run with production settings
java -Xms512m -Xmx1024m \
     -jar target/mind-maze-ai-0.0.1-SNAPSHOT.jar \
     --spring.profiles.active=prod
```

## Docker Deployment

### Single Container
```bash
# Build image
docker build -t mind-maze-ai .

# Run container
docker run -d \
  --name mind-maze \
  --network mind-maze-network \
  -p 8080:8080 \
  -v mind_maze_data:/app/data \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/MINDMAZE \
  -e SPRING_DATASOURCE_USERNAME=mind_maze \
  -e SPRING_DATASOURCE_PASSWORD=secure-password \
  -e SPRING_AI_OLLAMA_BASE_URL=http://ollama:11434 \
  -e JWT_SECRET=your-secure-jwt-key \
  --health-cmd="curl -f http://localhost:8080/actuator/health || exit 1" \
  --health-interval=30s \
  --restart unless-stopped \
  mind-maze-ai
```

### Docker Compose Stack
```yaml
version: '3.8'
services:
  app:
    image: mind-maze-ai
    networks:
      - mind-maze-network
    depends_on:
      postgres:
        condition: service_healthy
      ollama:
        condition: service_started
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/MINDMAZE
      - SPRING_AI_OLLAMA_BASE_URL=http://ollama:11434
    volumes:
      - mind_maze_data:/app/data
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
```

## Manual Deployment

### System Configuration
1. Create service user:
```bash
sudo useradd -r -s /bin/false mindmaze
```

2. Create application directories:
```bash
sudo mkdir -p /opt/mind-maze/{bin,config,data}
sudo chown -R mindmaze:mindmaze /opt/mind-maze
```

3. Copy application files:
```bash
sudo cp target/mind-maze-ai-*.jar /opt/mind-maze/bin/
sudo cp config/* /opt/mind-maze/config/
```

### Systemd Service
Create `/etc/systemd/system/mind-maze.service`:
```ini
[Unit]
Description=Mind Maze AI Server
After=network.target postgresql.service

[Service]
User=mindmaze
Group=mindmaze
WorkingDirectory=/opt/mind-maze
Environment=SPRING_PROFILES_ACTIVE=prod
Environment=JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"
ExecStart=/usr/bin/java $JAVA_OPTS -jar bin/mind-maze-ai.jar
Restart=always

[Install]
WantedBy=multi-user.target
```

### Start Service
```bash
sudo systemctl daemon-reload
sudo systemctl enable mind-maze
sudo systemctl start mind-maze
```

## Monitoring

### Health Check Endpoints
- Application Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`
- Info: `http://localhost:8080/actuator/info`

### Logging
- Application logs: `/opt/mind-maze/logs/`
- Container logs: `docker logs mind-maze`

## Security Notes
1. Always change default credentials
2. Use secure JWT secret in production
3. Enable HTTPS in production
4. Configure proper CORS settings
5. Set appropriate file permissions
6. Use non-root user in containers
