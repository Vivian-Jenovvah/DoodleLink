CREATE TABLE canvas_snapshots (
  id UUID PRIMARY KEY,
  room_id UUID NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
  snapshot_sequence_number BIGINT NOT NULL,
  schema_version INTEGER NOT NULL,
  snapshot_json JSONB NOT NULL,
  created_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_canvas_snapshots_room_sequence ON canvas_snapshots(room_id, snapshot_sequence_number DESC);
