package com.collaborativecanvas.server.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="canvas_snapshots")
public class CanvasSnapshotEntity {
 @Id public UUID id;
 @Column(name="room_id", nullable=false) public UUID roomId;
 @Column(name="snapshot_sequence_number", nullable=false) public long snapshotSequenceNumber;
 @Column(name="schema_version", nullable=false) public int schemaVersion;
 @JdbcTypeCode(SqlTypes.JSON) @Column(name="snapshot_json", nullable=false, columnDefinition="jsonb") public JsonNode snapshotJson;
 @Column(name="created_at", nullable=false) public Instant createdAt;
}
