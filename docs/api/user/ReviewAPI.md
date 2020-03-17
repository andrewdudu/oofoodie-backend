# Review API

## POST a review

- Endpoint : `/restaurants/{resto-id}/review`
- HTTP Method : `POST`

- Request Header :

  - Accept: `application/json`
  - Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg`

- Request Body :

```json
{
  "star": 5,
  "comment": "So nice"
}
```

- Response Body (Success) :

```json
{
  "code": 200,
  "status": "OK",
  "data": {
    "star": 5,
    "comment": "So nice"
  }
}
```

- Response Body (Fail) :

```json
{
  "timestamp": "2019-08-23T04:22:26.690+0000",
  "code": 404,
  "status": "Not Found",
  "message": "Restaurant with resto id {resto-id} is not found",
  "path": "/restaurants/{resto-id}"
}
```
