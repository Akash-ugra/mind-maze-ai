# API Endpoints Documentation

## Authentication
### POST /api/v1/auth/register
Register a new user
- Request: `CreateUserDTO`
- Response: `UserDTO`
- Status: 200 OK

### POST /api/v1/auth/login
Authenticate user
- Request: `LoginDTO`
- Response: `{ "token": "jwt-token" }`
- Status: 200 OK

## Quiz Management
### POST /api/v1/quiz/create
Create a new quiz
- Request: `CreateQuizDTO`
- Response: `QuizDTO`
- Status: 200 OK

### GET /api/v1/quiz
Get all quizzes for user
- Parameters: `userId`
- Response: `QuizDTO[]`
- Status: 200 OK

### GET /api/v1/quiz/{quizId}
Get specific quiz
- Parameters: `quizId`, `userId`
- Response: `QuizDTO`
- Status: 200 OK

### DELETE /api/v1/quiz
Delete a quiz
- Parameters: `quizId`, `userId`
- Response: Empty
- Status: 200 OK

## Question Management
### GET /api/v1/question/random
Get random question
- Parameters: `quizId`, `userId`
- Response: `QuestionDTO`
- Status: 200 OK

### POST /api/v1/question/saveProgress
Save answer and progress
- Parameters: `quizId`, `userId`, `questionId`, `selectedOption`
- Response: `CorrectResponseDTO`
- Status: 200 OK

### GET /api/v1/question/resume
Resume quiz
- Parameters: `quizId`, `userId`
- Response: `QuestionDTO`
- Status: 200 OK

### GET /api/v1/question/score
Get quiz score
- Parameters: `quizId`, `userId`
- Response: `ScoreDTO`
- Status: 200 OK
