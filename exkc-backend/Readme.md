# backend exkc

## get a token, example

Deprecated, but useful as simpler than Authentication Code Flow

```bash
curl -d 'client_id=exkc-app' -d 'username=exkc' -d 'password=exkc' -d 'grant_type=password' http://localhost:8088/realms/exkc/protocol/openid-connect/token
```

## examples, calls to endpoints

```bash
export TOKEN=<token>
curl -vvv -H "Authorization: Bearer $(echo $TOKEN)" -H "Content-Type: application/json" "http://localhost:8080/version"
```

```bash
export TOKEN=<token>
curl -vvv -H "Authorization: Bearer $(echo $TOKEN)" -H "Content-Type: application/json" "http://localhost:8080/quisuisje"
```