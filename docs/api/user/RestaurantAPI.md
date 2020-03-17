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

## Been There

- Endpoint : `/restaurants/been-there`
- HTTP Method : `POST`

- Request Header :

  - Accept: `application/json`
  - Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg`

- Request Body :

```json
{
  "resto-id": "4123-412312-412312"
}
```

- Response Body (Success) :

```json
{
  "code": 200,
  "status": "OK",
  "data": {
    "resto-id": "4123-412312-412312"
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
  "path": "/restaurants/been-there"
}
```

```json
{
  "timestamp": "2019-08-23T04:22:26.690+0000",
  "code": 404,
  "status": "Not Found",
  "message": "Restaurant Not found with id {resto-id}",
  "path": "/restaurants/been-there"
}
```

## Order Menu

- Endpoint : `/restaurants/order`
- HTTP Method : `POST`

- Request Header :

  - Accept: `application/json`
  - Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg`

- Request Body :

```json
{
  "resto-id": "14123-1412314-123",
  "menu": [
    {
      "name": "Ayam Bakar",
      "price": 25000,
      "qty": 3
    },
    {
      "name": "Ayam Bakar",
      "price": 25000,
      "qty": 3
    },
    {
      "name": "Ayam Bakar",
      "price": 25000,
      "qty": 3
    }
  ]
}
```

- Response Body (Success) :

```json
{
  "code": 200,
  "status": "OK",
  "data": [
    {
      "name": "Ayam Bakar",
      "price": 25000,
      "qty": 3
    },
    {
      "name": "Ayam Bakar",
      "price": 25000,
      "qty": 3
    },
    {
      "name": "Ayam Bakar",
      "price": 25000,
      "qty": 3
    }
  ]
}
```

- Response Body (Fail) :

```json
{
  "timestamp": "2019-08-23T04:22:26.690+0000",
  "code": 400,
  "status": "Bad Request",
  "message": "Invalid Request: Invalid request format",
  "path": "/restaurants/order"
}
```

```json
{
  "timestamp": "2019-08-23T04:22:26.690+0000",
  "code": 404,
  "status": "Not Found",
  "message": "Restaurant Not found with id {resto-id}",
  "path": "/restaurants/order"
}
```
