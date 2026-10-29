package com.collaborativecanvas.shared.dto;

import com.collaborativecanvas.shared.enums.ServerEventType;
import java.util.List;
public record CanvasStateMessage(String roomId, String participantId, ServerEventType type, int schemaVersion, long snapshotSequenceNumber,
                                 long lastSequenceNumber, CanvasDocument snapshot,
                                 List<DrawingOperationMessage> operationsAfterSnapshot) { }
