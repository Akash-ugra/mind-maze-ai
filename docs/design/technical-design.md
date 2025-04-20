# Technical Design Document

## System Architecture

### Components

1. **Web Layer**
   - REST Controllers
     - AuthController: User registration and authentication
     - QuizController: Quiz CRUD operations
     - QuestionController: Question management and progress tracking
   - Request DTOs
     - CreateUserDTO: User registration data
     - LoginDTO: Authentication credentials
     - CreateQuizDTO: Quiz creation parameters
   - Response DTOs
     - UserDTO: Safe user details
     - QuizDTO: Quiz metadata
     - QuestionDTO: Question without answers
     - ScoreDTO: Quiz attempt results
     - CorrectResponseDTO: Answer validation
   - Authentication Filter
     - JwtAuthenticationFilter: Token validation
     - SecurityConfig: Security rules
     - CorsConfig: Cross-origin settings

2. **Service Layer**
   - Authentication Services
     - CustomUserDetailsService: User lookup
     - UserService: User management
   - Quiz Services
     - QuizService: Quiz management
     - QuizProgressService: Progress tracking
     - OllamaQuizService: AI integration
   - Transaction Management
     - @Transactional on quiz operations
     - Atomic score updates
   - Async Processing
     - @Async quiz generation
     - Event-based status updates

3. **Data Layer**
   - Core Entities
     - CustomUserDetails: User data + security
     - Quiz: Quiz metadata
     - QuizQuestion: Questions + answers
     - QuizProgress: Attempt tracking
   - Repositories
     - UserRepository: User operations
     - QuizRepository: Quiz management
     - QuizQuestionRepo: Question handling
     - QuizProgressRepository: Progress tracking
   - Audit Support
     - Auditable base class
     - AuditableEntityListener
     - Automatic timestamp/user tracking

4. **Integration Layer**
   - AI Integration
     - OllamaChatModel configuration
     - Structured prompt templates
     - Response parsing
   - Security Integration
     - JWT generation/validation
     - BCrypt password encoding
     - Role-based access

### Security Details

1. **Authentication Flow**
   - Username/password validation
   - JWT token generation with claims
   - Token validation on requests
   - Role-based endpoint security

2. **Access Control**
   - Public endpoints: /api/v1/auth/login, /api/v1/auth/register, /swagger-ui.html, /v3/api-docs/xx, /swagger-ui/xx, /actuator/xx
   - Protected endpoints: all other endpoints
   - User-specific data filtering
   - Resource ownership validation

3. **Security Measures**
   - Password encryption with BCrypt
   - JWT with expiration
   - CORS with specific origins
   - Request validation using @Valid

### Asynchronous Processing

1. **Quiz Generation**
   - Async question generation
   - Status tracking (NOT_STARTED → IN_PROGRESS → SUCCESS/FAILURE)
   - Non-blocking user experience

2. **Progress Tracking**
   - Real-time score updates
   - Concurrent quiz attempts
   - Session management

### Error Handling

1. **Exception Types**
   - ResourceAlreadyExistsException: Duplicate resources
   - QuestionAlreadyAskedException: Quiz completion
   - QuizCreationException: AI generation failures

2. **Error Responses**
   - Structured ExceptionDTO
   - Appropriate HTTP status codes
   - Detailed error messages

### Data Flow Examples

1. **Quiz Creation Flow**
   ```
   Client → QuizController → QuizService → OllamaQuizService → AI
      ↓
   Database ← QuizRepository ← QuizService ← OllamaQuizService
   ```

2. **Question Flow**
   ```
   Client → QuestionController → QuizProgressService → QuestionRepo
      ↓
   Progress ← ProgressRepository ← QuizProgressService → Response
   ```

### Monitoring & Operations

1. **Health Checks**
   - Application health endpoint
   - Database connectivity
   - AI service availability

2. **Metrics**
   - Quiz completion rates
   - AI response times
   - Error rates

3. **Logging**
   - Structured JSON logging
   - Audit trail for changes
   - Error tracking
