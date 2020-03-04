# Product List API

## Add Restaurant

- Endpoint : `/restaurants`
- HTTP Method : `POST`

- Request Header :

  - Accept: `application/json`
  - Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg`

- Request Body :

```json
{
  "name": "The Magic of Nolem Gur",
  "open-hours": [
    {
      "day": "Sunday",
      "open": 1000,
      "close": 2300
    },
    {
      "day": "Monday",
      "open": 1000,
      "close": 2300
    },
    {
      "day": "Tuesday",
      "open": 1000,
      "close": 2300
    },
    {
      "day": "Wednesday",
      "open": 1000,
      "close": 2300
    },
    {
      "day": "Thursday",
      "open": 1000,
      "close": 2300
    },
    {
      "day": "Friday",
      "open": 1000,
      "close": 2300
    },
    {
      "day": "Saturday",
      "open": 1000,
      "close": 2300
    }
  ],
  "address": "Menara Rajawali, Lantai Ground, Jl. Dr Ide Anak Agung
Gde Agung, Kuningan, Jakarta",
  "type": "Casual Dining",
  "cuisine": "Western",
  "telephone": "021649879",
  "image": [
    "base64",
    "base64"
  ]
}
```

- Response Body (Success) :

```json
{
  "code": 200,
  "status": "OK",
  "data": {
    "id": "123-123",
    "product-id": "123-123",
    "image": "/image/voucher.png"
  }
}
```

- Response Body (Fail) :

```json
{
  "timestamp": "2019-08-23T04:22:26.690+0000",
  "code": 400,
  "status": "Bad Request",
  "message": "Invalid Request: Invalid request format",
  "path": "/restaurants"
}
```
