package com.collaborativecanvas.shared.dto;

import java.util.ArrayList;
import java.util.List;

public record CanvasDocument(int schemaVersion, double canvasWidth, double canvasHeight, List<CanvasStroke> strokes) {
    public CanvasDocument { strokes = strokes == null ? new ArrayList<>() : new ArrayList<>(strokes); }
    public static CanvasDocument empty() { return new CanvasDocument(1, 1200d, 800d, List.of()); }
}
