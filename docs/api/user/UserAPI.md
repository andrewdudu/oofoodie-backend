# User API

## GET User Information

- Endpoint : `/user/profile`
- HTTP Method : `GET`

- Request Header :

  - Accept: `application/json`
  - Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg`

- Response Body (Success) :

```json
{
  "image": "base64",
  "review": {
    "count": 12,
    "average-rating": 4.5,
    "visit": 32
  },
  "timeline": [
    {
      "type": "review",
      "star": 4,
      "comment": "Excellent food and very friendly staff! The chocolate
peanut butter cake is fantastic. I tried the salmon futtucini
and it was on point! Will definitely come back again!!",
      "like": 20,
      "time": 1313123213
    },
    {
      "type": "visit",
      "location": "DKI Jakarta, Indonesia",
      "like": 13,
      "time": 1312312312
    }
  ],
  "orders": {
    "incoming-orders": [
      {
        "id": "1231354-79456-46464",
        "name": "The Magic of Nolen Gur",
        "price": 283000,
        "star": 4.9
      }
    ],
    "on-going": [
      {
        "id": "1231354-79456-46464",
        "name": "The Magic of Nolen Gur",
        "price": 283000,
        "star": 4.9,
        "time": 123123
      }
    ],
    "history": [
      {
        "id": "1231354-79456-46464",
        "name": "The Magic of Nolen Gur",
        "price": 283000,
        "star": 4.9
      }
    ]
  }
}
```

## GET Order Information

- Endpoint : `/user/profile/order/{order-id}`
- HTTP Method : `GET`

- Request Header :

  - Accept: `application/json`
  - Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg`

- Response Body (Success) :

```json
{
  "id": "1231354-79456-46464",
  "name": "The Magic of Nolen Gur",
  "price": 283000,
  "star": 4.9,
  "menu": [
    {
      "name": "Red Wine",
      "price": 89000,
      "qty": 1
    },
    {
      "name": "Red Wine",
      "price": 89000,
      "qty": 1
    },
    {
      "name": "Red Wine",
      "price": 89000,
      "qty": 1
    },
    {
      "name": "Red Wine",
      "price": 89000,
      "qty": 1
    }
  ]
}
```

- Response Body (Fail) :

```json
{
  "timestamp": "2019-08-23T04:22:26.690+0000",
  "code": 404,
  "status": "Not Found",
  "message": "Order with id {order-id} is not found",
  "path": "/user/profile/order/{order-id}"
}
```

## Split Bill

- Endpoint : `/user/profile/split-bill`
- HTTP Method : `POST`

- Request Header :

  - Accept: `application/json`
  - Authorization: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqYXZhaW51c2UiLCJleHAiOjE1NjY1NTE5ODksImlhdCI6MTU2NjUzMzk4OX0.Kvx2VZkmckMexnTwK8A3vHSDar3J-K-dCrkJ2jmQtKdAWbw1dAjJ34WXCQXs-WO23OQPTqVF36E1STEhGZFZfg`

- Request Body :

```json
{
  "email": ["example@email.com", "example@email.com"]
}
```

- Response Body (Success) :

```json
{
  "success": true,
  "message": "Email has been sent"
}
```

- Response Body (Fail) :

```json
{
  "timestamp": "2019-08-23T04:22:26.690+0000",
  "code": 404,
  "status": "Not Found",
  "message": "Order with id {order-id} is not found",
  "path": "/user/profile/order/{order-id}"
}
```
