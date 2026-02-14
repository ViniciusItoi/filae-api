-- V6__Create_opening_hours_table.sql
CREATE TABLE IF NOT EXISTS opening_hours (
    id BIGSERIAL PRIMARY KEY,
    establishment_id BIGINT NOT NULL REFERENCES establishments(id) ON DELETE CASCADE,
    day_of_week VARCHAR(20) NOT NULL,
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL,
    is_closed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(establishment_id, day_of_week)
);

CREATE INDEX idx_opening_hours_establishment_id ON opening_hours(establishment_id);

