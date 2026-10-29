package com.collaborativecanvas.shared.dto;

import java.util.ArrayList;
import java.util.List;

public record CanvasStroke(String strokeId, String participantId, String colorHex, double brushSize, boolean isEraser,
                           List<CanvasPoint> points) {
    public CanvasStroke { points = points == null ? new ArrayList<>() : new ArrayList<>(points); }
}
