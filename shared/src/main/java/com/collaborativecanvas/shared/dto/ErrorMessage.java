package com.collaborativecanvas.shared.dto;

import java.time.Instant;
public record ErrorMessage(String code, String message, Instant timestamp, String requestId) { }
