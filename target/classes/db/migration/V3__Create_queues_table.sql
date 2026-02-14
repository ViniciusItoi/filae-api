-- V3__Create_queues_table.sql
CREATE TABLE IF NOT EXISTS queues (
    id BIGSERIAL PRIMARY KEY,
    ticket_number VARCHAR(50) UNIQUE NOT NULL,
    establishment_id BIGINT NOT NULL REFERENCES establishments(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    party_size INTEGER NOT NULL DEFAULT 1,
    notes TEXT,
    position INTEGER NOT NULL,
    total_in_queue INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'WAITING',
    estimated_wait_time INTEGER,
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    called_at TIMESTAMP WITH TIME ZONE,
    finished_at TIMESTAMP WITH TIME ZONE,
    cancelled_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_queues_establishment_id ON queues(establishment_id);
CREATE INDEX idx_queues_user_id ON queues(user_id);
CREATE INDEX idx_queues_status ON queues(status);
CREATE INDEX idx_queues_ticket_number ON queues(ticket_number);

