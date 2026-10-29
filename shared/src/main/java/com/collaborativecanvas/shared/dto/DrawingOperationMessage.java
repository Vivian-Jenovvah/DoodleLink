package com.collaborativecanvas.shared.dto;

import com.collaborativecanvas.shared.enums.DrawingOperationType;
import java.time.Instant;
import java.util.UUID;

public record DrawingOperationMessage(UUID operationId, String roomId, String participantId, DrawingOperationType type,
                                      String strokeId, double normalizedX, double normalizedY, String colorHex,
                                      double brushSize, boolean isEraser, Long sequenceNumber, Instant clientTimestamp,
                                      Instant serverTimestamp) { }
