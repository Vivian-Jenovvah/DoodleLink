package com.collaborativecanvas.server.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="rooms")
public class RoomEntity {
 @Id public UUID id;
 @Column(name="room_code", unique=true, length=12, nullable=false) public String roomCode;
 @Column(nullable=false, length=20) public String status;
 @Column(name="last_sequence_number", nullable=false) public long lastSequenceNumber;
 @Column(name="created_at", nullable=false) public Instant createdAt;
 @Column(name="updated_at", nullable=false) public Instant updatedAt;
 @Column(name="last_activity_at", nullable=false) public Instant lastActivityAt;
 @Column(name="expires_at", nullable=false) public Instant expiresAt;
}
