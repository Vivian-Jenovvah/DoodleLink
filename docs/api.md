# REST API

Base path: `/api/v1`.

- `POST /rooms` with `{ "displayName": "Alex" }` returns `201` and a public `roomId`.
- `GET /rooms/{roomId}` returns active room metadata.
- `GET /actuator/health` is the conservative Spring Boot health endpoint.

Errors use `ErrorMessage`: `code`, safe `message`, `timestamp`, and `requestId`. User input is trimmed, display names are limited to 32 characters, and invalid/expired room codes produce safe `404` responses.
