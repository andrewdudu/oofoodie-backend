# User API

## GET User Information

- Endpoint : `/user/profile/{user-id}`
- HTTP Method : `GET`

- Request Header :

  - Accept: `application/json`

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
  ]
}
```
