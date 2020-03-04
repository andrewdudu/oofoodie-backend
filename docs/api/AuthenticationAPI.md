# Authentication API

## User/Merchant/Admin Login

- Endpoint : `/login`
- HTTP Method : `POST`
- Request Body :

```json
{
  "usernameOrEmail": "andrewdudu",
  "password": "admin123"
}
```

- Request Header : + Accept : `application/json`
- Response Body (Success) :

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg"
}
```

- Response Body (Fail) :

```json
{
  "timestamp": "2019-08-23T04:22:26.690+0000",
  "code": 400,
  "status": "Bad Request",
  "message": "Invalid Request: Invalid user authentication or invalid request format",
  "path": "/login"
}
```

```json
{
  "timestamp": "2019-08-23T04:22:26.690+0000",
  "code": 401,
  "status": "Unauthorized",
  "message": "Invalid Request: username/email or password is wrong",
  "path": "/login"
}
```
