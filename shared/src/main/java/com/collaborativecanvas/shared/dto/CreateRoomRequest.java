package com.collaborativecanvas.shared.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record CreateRoomRequest(@NotBlank @Size(max = 32) String displayName) { }
