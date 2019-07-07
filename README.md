#service-authentication
Microservice for stateless token generation and renewal.

## External API
| API URI  | Description | Consumes | Produces |
| ------------- | ------------- | ------------- | ------------- |
| `GET /access_token`   | Consumes a refresh token and returns a new access token.  | Refresh Token | Access Token |

## Internal API
| API URI  | Description | Consumes | Produces |
| ------------- | ------------- | ------------- | ------------- |
| `GET /refresh_token`  | Creates a new ∞ refresh token and complimentary access token. | `UserObj` | `TokenPair` |

## Objects
### Refresh Token
The refresh token stores just enough data to create an access token. The refresh and access tokens are known to be
different by the `role` key in the payload.
```
{
    "user_id": x,
    "role": "REFRESH"
}
```

### Access Token
The access token holds user specific data. It comes in two forms: pre-registration ("connect") phase, and post-registration.
The difference lies in that a unconnected user will not have a partner nor couple id.
```
{
    "user_id": x,
    "connect_code": "a5ns2AksN" // generated for each user,
    "role": "ACCESS"
}
```

```
{
    "user_id": x,
    "partner_id": y,
    "couple_id": z,
    "role": "ACCESS"
}
```

### `UserObj`
All user object data is stored in the payload of the token, and each of the three variables are passed as headers.
If the `partner_id` is not present, a connect code is generated and stored instead of the `couple_id`.
```
"user_id": x
"partner_id"?: y // optional
"couple_id"?: z  // optional
```

### `TokenPair`
A json object with two keys, `refresh_token` and `access_token`.
```
{
    "refresh_token": "xxx.yyy.zzz",
    "access_token": "zzz.yyy.xxx"
}
```

## Examples
### Pre-connect Token
```
GET: /refresh_token
HEADER: user_id=123123
------
RESPONSE: 200 {
  "refresh_token": "xxx.yyy.zzz",
  "access_token": "zzz.yyy.xxx" // includes user_id, connect_code
}
```

### Post-connect Token
```
GET: /refresh_token
HEADER: user_id=123123
HEADER: partner_id=1231111
HEADER: couple_id=1011010
------
RESPONSE: 200 {
    "refresh_token": "xxx.yyy.zzz",
    "access_token": "zzz.yyy.xxx" // includes user_id, partner_id, couple_id
}
```

### Refreshing the Access Token
```
GET: /access_token
HEADER: refresh_token=xxx.yyy.zzz
------
RESPONSE 200 {
    "access_token": "zzz.yyy.xxx"
}
```