# backend exkc Api

## get token

curl --location --request POST 'http://localhost:8088/realms/exkc/protocol/openid-connect/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'client_id=exkc-partner' \
--data-urlencode 'client_secret=LOGQJV0Pqn5S4Ivytlo6vC745uwzV7mE' \
--data-urlencode 'scope=email' \
--data-urlencode 'grant_type=client_credentials'

## exemples

```bash
export TOKEN=<token>
curl -vvv -H "Authorization: Bearer $(echo $TOKEN)" -H "Content-Type: application/json" http://localhost:8082/exkc/version
```