CREATE TABLE drawing_operations (
  id UUID PRIMARY KEY,
  room_id UUID NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
  operation_id UUID NOT NULL,
  sequence_number BIGINT NOT NULL,
  participant_id VARCHAR(64) NOT NULL,
  operation_type VARCHAR(30) NOT NULL,
  payload_json JSONB NOT NULL,
  created_at TIMESTAMPTZ NOT NULL,
  CONSTRAINT uq_drawing_operation_id UNIQUE(room_id, operation_id),
  CONSTRAINT uq_drawing_sequence UNIQUE(room_id, sequence_number)
);
CREATE INDEX idx_drawing_operations_room_sequence ON drawing_operations(room_id, sequence_number);
