-- V2__Create_establishments_table.sql
CREATE TABLE IF NOT EXISTS establishments (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100) NOT NULL,
    address VARCHAR(500) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip_code VARCHAR(10),
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    phone_number VARCHAR(20),
    email VARCHAR(255),
    website VARCHAR(500),
    photo_url VARCHAR(500),
    rating DECIMAL(3, 2) DEFAULT 0,
    review_count INTEGER DEFAULT 0,
    is_accepting_customers BOOLEAN DEFAULT TRUE,
    queue_enabled BOOLEAN DEFAULT TRUE,
    merchant_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_establishments_category ON establishments(category);
CREATE INDEX idx_establishments_merchant_id ON establishments(merchant_id);
CREATE INDEX idx_establishments_city ON establishments(city);
CREATE INDEX idx_establishments_coordinates ON establishments(latitude, longitude);

