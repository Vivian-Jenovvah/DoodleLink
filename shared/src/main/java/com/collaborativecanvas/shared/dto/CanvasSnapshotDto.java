package com.collaborativecanvas.shared.dto;

public record CanvasSnapshotDto(long snapshotSequenceNumber, int schemaVersion, CanvasDocument document) { }
