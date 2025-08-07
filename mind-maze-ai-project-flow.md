# Mind Maze AI - Project Flow & Architecture

## 1. High-Level Architecture

```mermaid
graph TB
    Client[Web/Mobile Client] --> API[REST API Layer]
    API --> Security{JWT Security Filter}
    Security --> Auth[Authentication Service]
    Security --> Quiz[Quiz Service]
    Security --> Progress[Progress Service]
    
    Auth --> UserRepo[(User Repository)]
    Quiz --> QuizRepo[(Quiz Repository)]
    Quiz --> QuestionRepo[(Question Repository)]
    Quiz --> AIService[Ollama Quiz Service]
    AIService --> Ollama[Ollama Server]
    Progress --> ProgressRepo[(Progress Repository)]
    
    subgraph Database
        UserRepo
        QuizRepo
        QuestionRepo
        ProgressRepo
    end
```

---

## 2. Detailed Component Diagram

```mermaid
graph TD
    subgraph Frontend
        Client[Web/Mobile Client]
    end
    
    subgraph Security_Layer
        JWTFilter[JWT Authentication Filter]
        SecurityConfig[Security Config]
        JWTUtil[JWT Utilities]
    end
    
    subgraph API_Layer
        Auth[Auth Controller]
        Quiz[Quiz Controller]
        Question[Question Controller]
        GlobalError[Global Exception Handler]
    end
    
    subgraph Service_Layer
        AuthService[Auth Service]
        UserService[User Service]
        QuizService[Quiz Service]
        ProgressService[Quiz Progress Service]
        AIService[Ollama Quiz Service]
    end
    
    subgraph Repository_Layer
        UserRepo[User Repository]
        QuizRepo[Quiz Repository]
        QuestionRepo[Question Repository]
        ProgressRepo[Progress Repository]
    end
    
    subgraph External
        DB[(PostgreSQL)]
        Ollama[Ollama AI]
    end

    Client --> JWTFilter
    JWTFilter --> SecurityConfig
    SecurityConfig --> API_Layer
    Auth --> AuthService & UserService
    Quiz --> QuizService
    Question --> ProgressService
    AuthService --> UserRepo & JWTUtil
    UserService --> UserRepo
    QuizService --> QuizRepo & QuestionRepo & AIService
    ProgressService --> ProgressRepo & QuestionRepo
    AIService --> Ollama
    UserRepo --> DB
    QuizRepo --> DB
    QuestionRepo --> DB
    ProgressRepo --> DB

    style SecurityConfig fill:#f9f,stroke:#333
    style JWTFilter fill:#f9f,stroke:#333
    style GlobalError fill:#ff9,stroke:#333
```

---

## 3. Data Flow Example (Quiz Creation & Retrieval)

```mermaid
sequenceDiagram
    participant Client
    participant QuizController
    participant QuizService
    participant OllamaQuizService
    participant QuizRepository
    participant DB
    participant Ollama

    Client->>QuizController: POST /api/v1/quiz (create quiz)
    QuizController->>QuizService: createQuiz()
    QuizService->>OllamaQuizService: Generate questions (async)
    OllamaQuizService->>Ollama: Request AI-generated questions
    Ollama-->>OllamaQuizService: AI-generated questions
    OllamaQuizService-->>QuizService: Questions generated
    QuizService->>QuizRepository: Save questions & Update status
    QuizRepository->>DB: Save questions and status
    DB-->>QuizRepository: Confirm save
    QuizRepository-->>QuizService: Return quiz
    QuizService-->>QuizController: Return QuizDTO
    QuizController-->>Client: 200 OK + QuizDTO

    %% Get Quiz Flow
    Client->>QuizController: GET /api/v1/quiz/{quizId}
    QuizController->>QuizService: getQuizByIdAndUser()
    QuizService->>QuizRepository: findByIdAndUser()
    QuizRepository->>DB: Fetch quiz
    DB-->>QuizRepository: Return quiz
    QuizRepository-->>QuizService: Return quiz
    QuizService-->>QuizController: Return QuizDTO
    QuizController-->>Client: 200 OK + QuizDTO
```

---

## 4. Database Schema (Entity Relationship Overview)

```mermaid
erDiagram
    APP_USER ||--o{ USER_AUTHORITIES : has
    APP_USER ||--o{ QUIZ : creates
    APP_USER ||--o{ QUIZ_PROGRESS : tracks
    QUIZ ||--o{ QUIZ_QUESTION : contains
    QUIZ ||--|{ QUIZ_PROGRESS : tracks_progress
    QUIZ_QUESTION ||--o{ QUIZ_OPTIONS : has_answers
    QUIZ_PROGRESS }o--o{ QUIZ_PROGRESS_ASKED_QUESTIONS : tracks_asked
```

---

## 5. Key Features

- JWT-based authentication
- AI-powered quiz generation (Ollama integration)
- Async quiz processing
- RESTful API endpoints
- PostgreSQL data storage
- Health/metrics monitoring

---

> Use these diagrams and explanations to clearly communicate the flow and design of your project to your teacher.