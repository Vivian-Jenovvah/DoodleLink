# Database

Flyway migrations create `rooms`, `canvas_snapshots`, and `drawing_operations`. Snapshot and operation payloads use PostgreSQL JSONB. Operation uniqueness is enforced for `(room_id, operation_id)` and `(room_id, sequence_number)`, making retries idempotent and ordering durable.

Rooms are marked `EXPIRED` after the configured inactive lifetime only when no participant is connected. Expired rooms are deleted after `ROOM_RETENTION_GRACE_DAYS` (seven by default); PostgreSQL foreign-key cascades remove their snapshots and operations. Snapshot retention keeps the newest three snapshots by default, while operation records remain available for reconstruction from every retained snapshot.

For local reset, run `docker compose down -v` only when you intentionally want to delete local drawing data. Never use that command against a production database.
