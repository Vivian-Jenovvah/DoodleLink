# Architecture

`shared` owns DTOs, enums and API/destination constants. `server` is authoritative: each room has an in-process fair lock plus a PostgreSQL pessimistic room lock. It assigns a sequence number, persists the operation and room update in one transaction, then broadcasts it. `client` is JavaFX/FXML and talks only to REST/STOMP contracts in `shared`.

For synchronization, the server takes the room lock, records its current sequence `S`, loads the latest snapshot at or before `S`, and loads the persisted operations through `S`. The joining session is already subscribed before it asks to join; it applies that state before enabling input. Later operations have sequence numbers greater than `S`; the client buffers out-of-order messages and requests a complete resync if it sees a gap. The user-specific state response also carries the server-assigned participant ID; clients use it to mark only their own entry in a shared presence broadcast.

Snapshots are initial (sequence 0), every 100 operations, and every 30 seconds while dirty. The three newest snapshots are retained by default. Erasers are canonical strokes rendered in white over the white logical canvas, producing the same result on every JavaFX client.
