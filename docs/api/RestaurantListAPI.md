# Product List API

## GET Vouchers List

- Endpoint : `/vouchers`
- HTTP Method : `GET`

- Request Header :
  - Accept: `application/json`
- Response Body (Success) :

```json
{
  "code": 200,
  "status": "OK",
  "data": [
    {
      "id": "123-123",
      "product-id": "123-123",
      "image": "/image/voucher.png"
    },
    {
      "id": "123-123",
      "product-id": "123-123",
      "image": "/image/voucher.png"
    }
  ]
}
```

## GET Popular Restaurants

- Endpoint : `/restaurants/popular`
- HTTP Method : `GET`
- Request Header :
  - Accept: `application/json`
- Response Body (Success) :

```json
{
  "code": 200,
  "status": "OK",
  "data": [
    {
      "id": "123-123",
      "name": "The Magic of Nolem Gur",
      "likes": 21,
      "average-star": 4.9,
      "image": "/image/popular.png"
    },
    {
      "id": "123-123",
      "name": "The Magic of Nolem Gur",
      "likes": 21,
      "average-star": 4.9,
      "image": "/image/popular.png"
    }
  ]
}
```

## GET Nearby Restaurants

- Endpoint : `/restaurants/nearby`
- HTTP Method : `GET`
- Request Header :
  - Accept: `application/json`
- Response Body (Success) :

```json
{
  "code": 200,
  "status": "OK",
  "data": [
    {
      "id": "123-123",
      "name": "The Magic of Nolem Gur",
      "likes": 21,
      "average-star": 4.9,
      "image": "/image/popular.png"
    },
    {
      "id": "123-123",
      "name": "The Magic of Nolem Gur",
      "likes": 21,
      "average-star": 4.9,
      "image": "/image/popular.png"
    }
  ]
}
```
