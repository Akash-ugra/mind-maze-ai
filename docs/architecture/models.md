# Data Models

## Request DTOs

### CreateUserDTO
```json
{
  "username": "string (required)",
  "password": "string (required)",
  "name": "string (required)",
  "email": "string (required, valid email format)"
}
```

### LoginDTO
```json
{
  "username": "string (required)",
  "password": "string (required)"
}
```

### CreateQuizDTO
```json
{
  "quizType": "enum[MATH,SCIENCE,HISTORY,GEOGRAPHY,LITERATURE,ART,MUSIC,TECHNOLOGY,SPORTS,GENERAL_KNOWLEDGE]",
  "quizLevel": "enum[EASY,MEDIUM,HARD,EXPERT]",
  "numberOfQuestions": "integer (required)"
}
```

## Response DTOs

### UserDTO
```json
{
  "id": "long",
  "username": "string",
  "name": "string",
  "email": "string",
  "roles": "Set<string> (e.g., ROLE_USER)"
}
```

### QuizDTO
```json
{
  "quizId": "string (UUID)",
  "quizType": "string",
  "quizLevel": "string",
  "creationStatus": "string (NOT_STARTED,IN_PROGRESS,SUCCESS,FAILURE)", 
  "numberOfQuestions": "integer"
}
```

### QuestionDTO
```json
{
  "id": "string (UUID)",
  "question": "string",
  "options": "string[] (4 possible answers)"
}
```

### CorrectResponseDTO
```json
{
  "isCorrect": "boolean",
  "correctOption": "string",
  "question": "string"
}
```

### ScoreDTO
```json
{
  "correctAnswers": "integer",
  "wrongAnswers": "integer", 
  "totalQuestions": "integer"
}
```

### ExceptionDTO
```json
{
  "message": "string",
  "status": "integer (HTTP status code)"
}
```

## Internal Models

### QuizResponse (AI Model Response)
```json
{
  "quizId": "string",
  "quizType": "string",
  "quizLevel": "string",
  "numberOfQuestions": "integer",
  "quizQuestionList": [
    {
      "question": "string",
      "options": "string[] (4 options)",
      "answer": "string"
    }
  ]
}
```
