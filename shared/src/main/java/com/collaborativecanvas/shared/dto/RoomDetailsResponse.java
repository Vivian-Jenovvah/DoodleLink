package com.collaborativecanvas.shared.dto;

import java.time.Instant;
public record RoomDetailsResponse(String roomId, String status, int participantCount, long lastSequenceNumber, Instant createdAt, Instant lastActivityAt) { }
