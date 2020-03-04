# Product API

## GET Restaurant by ID

- Endpoint : `/restaurants/{resto-id}`
- HTTP Method : `GET`

- Request Header :
  - Accept: `application/json`
- Response Body (Success) :

```json
{
  "code": 200,
  "status": "OK",
  "data": {
    "name": "The Magic of Nolem Gur",
    "open-hours": [
      {
        "day": "Sunday",
        "open": "09:00 AM",
        "close": "10:00 PM"
      },
      {
        "day": "Monday",
        "open": "09:00 AM",
        "close": "10:00 PM"
      },
      {
        "day": "Tuesday",
        "open": "09:00 AM",
        "close": "10:00 PM"
      },
      {
        "day": "Wednesday",
        "open": "09:00 AM",
        "close": "10:00 PM"
      },
      {
        "day": "Thursday",
        "open": "09:00 AM",
        "close": "10:00 PM"
      },
      {
        "day": "Friday",
        "open": "09:00 AM",
        "close": "10:00 PM"
      },
      {
        "day": "Saturday",
        "open": "09:00 AM",
        "close": "10:00 PM"
      }
    ],
    "address": "Menara Rajawali, Lantai Ground, Jl. Dr Ide Anak Agung Gde Agung, Kuningan, Jakarta",
    "type": "Casual Dining",
    "cuisine": "Western",
    "telephone": "021649879",
    "image": "base64",
    "review": {
      "star-percentage": {
        "one": 1,
        "two": 3,
        "three": 2,
        "four": 5,
        "five": 89
      },
      "average-star": 4.9,
      "total-review": 102
    }
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
