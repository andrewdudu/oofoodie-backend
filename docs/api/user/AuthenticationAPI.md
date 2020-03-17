# Authentication API

## User Sign Up

- Endpoint : `/user/register`
- HTTP Method : `POST`
- Request Body :

```json
{
  "name": "Andrew",
  "username": "andrewdudu",
  "email": "andrew@gmail.com",
  "password": "admin123"
}
```

- Request Header : + Accept: `application/json`
- Response Body (Success) :

```json
{
  "success": "true",
  "message": "User registered successfully"
}
```

- Response Body (Fail) :

```json
{
  "timestamp": "2019-08-23T04:22:26.690+0000",
  "code": 400,
  "status": "Bad Request",
  "message": "Invalid Request: Invalid request format",
  "path": "/user/register"
}
```
