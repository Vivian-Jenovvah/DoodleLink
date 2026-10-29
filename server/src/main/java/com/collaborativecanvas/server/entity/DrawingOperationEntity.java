package com.collaborativecanvas.server.entity;

import com.collaborativecanvas.shared.enums.DrawingOperationType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="drawing_operations", uniqueConstraints={@UniqueConstraint(columnNames={"room_id","operation_id"}), @UniqueConstraint(columnNames={"room_id","sequence_number"})})
public class DrawingOperationEntity {
 @Id public UUID id;
 @Column(name="room_id", nullable=false) public UUID roomId;
 @Column(name="operation_id", nullable=false) public UUID operationId;
 @Column(name="sequence_number", nullable=false) public long sequenceNumber;
 @Column(name="participant_id", nullable=false, length=64) public String participantId;
 @Enumerated(EnumType.STRING) @Column(name="operation_type", nullable=false, length=30) public DrawingOperationType operationType;
 @JdbcTypeCode(SqlTypes.JSON) @Column(name="payload_json", nullable=false, columnDefinition="jsonb") public JsonNode payloadJson;
 @Column(name="created_at", nullable=false) public Instant createdAt;
}
