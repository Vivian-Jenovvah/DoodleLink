package com.collaborativecanvas.shared.dto;

import java.time.Instant;
public record ParticipantDto(String roomId, String participantId, String displayName, boolean currentUser, int activeParticipantCount, Instant serverTimestamp) { }
