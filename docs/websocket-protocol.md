# WebSocket protocol

Connect STOMP to `/ws`; application destinations begin `/app`, broadcasts begin `/topic`, and per-session messages are delivered to `/user/queue/messages`.

| Action | Destination |
|---|---|
| Join/sync | `/app/rooms/{roomId}/join`, `/app/rooms/{roomId}/sync` |
| Operation | `/app/rooms/{roomId}/operations` |
| Leave | `/app/rooms/{roomId}/leave` |
| Canvas/presence subscriptions | `/topic/rooms/{roomId}/canvas`, `/topic/rooms/{roomId}/participants` |

Join carries `JoinRoomRequest`; operation messages are `DrawingOperationMessage`. The server ignores client identity and takes it from the WebSocket membership association. Canvas state uses `CanvasStateMessage` containing a typed snapshot, typed replay list, and the server-assigned `participantId` for that user-specific response. Presence broadcasts deliberately set `currentUser` false: each client compares the returned participant ID itself, so a shared broadcast never marks another participant as the local user. The client uses Spring's maintained STOMP WebSocket client behind its `StompClient` interface.
