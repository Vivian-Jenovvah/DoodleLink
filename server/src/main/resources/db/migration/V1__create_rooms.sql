CREATE TABLE rooms (
  id UUID PRIMARY KEY,
  room_code VARCHAR(12) NOT NULL UNIQUE,
  status VARCHAR(20) NOT NULL,
  last_sequence_number BIGINT NOT NULL DEFAULT 0,
  created_at TIMESTAMPTZ NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL,
  last_activity_at TIMESTAMPTZ NOT NULL,
  expires_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_rooms_code ON rooms(room_code);
CREATE INDEX idx_rooms_expiry ON rooms(status, expires_at);
