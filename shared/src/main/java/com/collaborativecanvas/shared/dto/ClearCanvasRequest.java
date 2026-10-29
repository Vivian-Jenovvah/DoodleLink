package com.collaborativecanvas.shared.dto;
import java.util.UUID;
public record ClearCanvasRequest(UUID operationId, String roomId) { }
