# Collaborative Canvas

Collaborative Canvas is a Java 21 desktop application: a JavaFX/FXML client sends typed STOMP operations to a Spring Boot service, which persists PostgreSQL operations and versioned JSONB snapshots before broadcasting committed updates.

## Prerequisites

- JDK 21 (set `JAVA_HOME` to it)
- Docker Desktop for local PostgreSQL
- Gradle 8.7+ initially, or use the generated Gradle wrapper after it has been bootstrapped in your environment

## Run locally

PowerShell:

```powershell
Copy-Item .env.example .env   # optional reference only; Docker Compose has safe local defaults
docker compose up -d postgres
.\gradlew.bat :server:bootRun
# in a second terminal
.\gradlew.bat :client:run
```

On macOS/Linux use `./gradlew :server:bootRun` and `./gradlew :client:run`. The client defaults to `http://localhost:8080`; use `-Dcanvas.serverUrl=https://host` to override it. Start a second client to collaborate.

## Build and test

```powershell
.\gradlew.bat test
.\gradlew.bat build
```

The PostgreSQL Testcontainers test runs when Docker is available. The remaining unit tests do not require a database. Flyway is the only schema owner: the server deliberately fails rather than falling back to an in-memory database if PostgreSQL is unreachable.

## User flow

1. Enter a display name and server URL, then create a room or enter an existing room ID.
2. The workspace waits for a server `CANVAS_STATE` message before enabling drawing.
3. Draw with the brush/eraser, select a color and brush size, or use **Clear Canvas** (which is confirmed and sequenced).
4. Share the room ID. A later joiner reconstructs the document from the persisted snapshot plus ordered operation replay.

## Configuration and security notes

`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `SERVER_PORT`, `ROOM_INACTIVE_TTL_HOURS`, `CANVAS_SNAPSHOT_OPERATION_THRESHOLD`, and `CANVAS_SNAPSHOT_INTERVAL_SECONDS` are supported; see `.env.example`. Rooms expire after 24 inactive hours by default. This MVP has no authentication: room IDs are join secrets, not strong access control. Use HTTPS/WSS behind a TLS reverse proxy in deployed environments.

## Verification boundaries

The project includes the real PostgreSQL/Flyway configuration and Testcontainers coverage. Manual two-client UI verification still requires a local JDK 21, Gradle and Docker. In restricted Windows shells, Gradle may need permission to use its standard native cache; this is an environment constraint, not a runtime application dependency.
