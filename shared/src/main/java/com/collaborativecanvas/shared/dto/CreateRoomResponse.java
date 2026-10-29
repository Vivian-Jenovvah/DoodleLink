package com.collaborativecanvas.shared.dto;

import java.time.Instant;
public record CreateRoomResponse(String roomId, String participantId, String displayName, String status, long lastSequenceNumber, Instant createdAt) { }
