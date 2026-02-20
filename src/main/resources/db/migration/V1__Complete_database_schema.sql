-- =============================================================================
-- V1__Complete_database_schema.sql
-- Complete database schema and test data for Filae API - All-in-One Migration
-- =============================================================================

-- =============================================================================
-- PART 1: CREATE TABLES
-- =============================================================================

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    profile_picture_url VARCHAR(500),
    user_type VARCHAR(50) NOT NULL DEFAULT 'CUSTOMER',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_user_type ON users(user_type);

-- Establishments Table
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

-- Queues Table
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

-- Favorites Table
CREATE TABLE IF NOT EXISTS favorites (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    establishment_id BIGINT NOT NULL REFERENCES establishments(id) ON DELETE CASCADE,
    added_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, establishment_id)
);

CREATE INDEX idx_favorites_user_id ON favorites(user_id);
CREATE INDEX idx_favorites_establishment_id ON favorites(establishment_id);

-- Notifications Table
CREATE TABLE IF NOT EXISTS notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    ticket_id BIGINT REFERENCES queues(id) ON DELETE CASCADE,
    establishment_id BIGINT REFERENCES establishments(id) ON DELETE CASCADE,
    type VARCHAR(100) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);
CREATE INDEX idx_notifications_created_at ON notifications(created_at);

-- Opening Hours Table
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

-- =============================================================================
-- PART 2: INSERT TEST DATA
-- =============================================================================

-- Clear existing data (if migration is re-run)
DELETE FROM queues;
DELETE FROM notifications;
DELETE FROM favorites;
DELETE FROM opening_hours;
DELETE FROM establishments;
DELETE FROM users;

-- Reset sequences
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE establishments_id_seq RESTART WITH 1;
ALTER SEQUENCE queues_id_seq RESTART WITH 1;
ALTER SEQUENCE favorites_id_seq RESTART WITH 1;
ALTER SEQUENCE notifications_id_seq RESTART WITH 1;
ALTER SEQUENCE opening_hours_id_seq RESTART WITH 1;

-- ============================================
-- INSERT TEST USERS (6 total)
-- ============================================
-- All passwords: SecurePass123!
-- Hash: $2a$10$3SFNPU2z7Jt3AQI6K.1s2ey4R1.5ymSBPqGtqJLSxPk2EsKFteFd2

INSERT INTO users (name, email, password_hash, phone, user_type, is_active)
VALUES ('Alice Silva', 'alice@example.com', '$2a$10$3SFNPU2z7Jt3AQI6K.1s2ey4R1.5ymSBPqGtqJLSxPk2EsKFteFd2', '11998765432', 'CUSTOMER', TRUE);

INSERT INTO users (name, email, password_hash, phone, user_type, is_active)
VALUES ('Bob Santos', 'bob@example.com', '$2a$10$3SFNPU2z7Jt3AQI6K.1s2ey4R1.5ymSBPqGtqJLSxPk2EsKFteFd2', '11987654321', 'CUSTOMER', TRUE);

INSERT INTO users (name, email, password_hash, phone, user_type, is_active)
VALUES ('Carol Oliveira', 'carol@example.com', '$2a$10$3SFNPU2z7Jt3AQI6K.1s2ey4R1.5ymSBPqGtqJLSxPk2EsKFteFd2', '11976543210', 'CUSTOMER', TRUE);

INSERT INTO users (name, email, password_hash, phone, user_type, is_active)
VALUES ('Tony Rossi', 'tony@tonysrestaurant.com', '$2a$10$3SFNPU2z7Jt3AQI6K.1s2ey4R1.5ymSBPqGtqJLSxPk2EsKFteFd2', '1133334444', 'MERCHANT', TRUE);

INSERT INTO users (name, email, password_hash, phone, user_type, is_active)
VALUES ('Maria Café', 'maria@mariacafe.com', '$2a$10$3SFNPU2z7Jt3AQI6K.1s2ey4R1.5ymSBPqGtqJLSxPk2EsKFteFd2', '1144445555', 'MERCHANT', TRUE);

INSERT INTO users (name, email, password_hash, phone, user_type, is_active)
VALUES ('System Admin', 'admin@filae.com', '$2a$10$3SFNPU2z7Jt3AQI6K.1s2ey4R1.5ymSBPqGtqJLSxPk2EsKFteFd2', '1155556666', 'ADMIN', TRUE);

-- ============================================
-- INSERT TEST ESTABLISHMENTS (5 total)
-- ============================================

INSERT INTO establishments
(name, description, category, address, city, state, zip_code, phone_number, email, is_accepting_customers, queue_enabled, merchant_id, rating, review_count)
VALUES
('Tony''s Restaurant', 'Italian cuisine with traditional recipes from Naples', 'restaurant', 'Rua Augusta, 1500', 'São Paulo', 'SP', '01305-100', '1133334444', 'tony@tonysrestaurant.com', TRUE, TRUE, 4, 4.8, 156);

INSERT INTO establishments
(name, description, category, address, city, state, zip_code, phone_number, email, is_accepting_customers, queue_enabled, merchant_id, rating, review_count)
VALUES
('Maria''s Café', 'Cozy café with specialty coffee and pastries', 'café', 'Av. Paulista, 800', 'São Paulo', 'SP', '01311-100', '1144445555', 'maria@mariacafe.com', TRUE, TRUE, 5, 4.6, 89);

INSERT INTO establishments
(name, description, category, address, city, state, zip_code, phone_number, email, is_accepting_customers, queue_enabled, merchant_id, rating, review_count)
VALUES
('Fast Burgers', 'Premium burgers and craft beers', 'fast food', 'Rua Oscar Freire, 500', 'São Paulo', 'SP', '01426-100', '1155557777', 'contact@fastburgers.com', TRUE, TRUE, 4, 4.3, 234);

INSERT INTO establishments
(name, description, category, address, city, state, zip_code, phone_number, email, is_accepting_customers, queue_enabled, merchant_id, rating, review_count)
VALUES
('Barber Shop Plus', 'Modern barbershop with experienced stylists', 'barbershop', 'Rua Consolação, 2000', 'São Paulo', 'SP', '01302-100', '1166668888', 'barbershop@plus.com', TRUE, TRUE, 5, 4.9, 312);

INSERT INTO establishments
(name, description, category, address, city, state, zip_code, phone_number, email, is_accepting_customers, queue_enabled, merchant_id, rating, review_count)
VALUES
('Cinema Downtown', 'Modern cinema with 8 screens and premium sound', 'cinema', 'Av. Rio Branco, 1200', 'São Paulo', 'SP', '01401-100', '1177779999', 'info@cinemadowntown.com', TRUE, TRUE, 4, 4.7, 445);

-- ============================================
-- INSERT TEST QUEUES (7 total)
-- ============================================

INSERT INTO queues (ticket_number, establishment_id, user_id, party_size, position, total_in_queue, status, estimated_wait_time)
VALUES ('TA-A1B2', 1, 1, 2, 1, 5, 'WAITING', 10);

INSERT INTO queues (ticket_number, establishment_id, user_id, party_size, position, total_in_queue, status, estimated_wait_time)
VALUES ('TA-A1C3', 1, 2, 1, 2, 5, 'WAITING', 20);

INSERT INTO queues (ticket_number, establishment_id, user_id, party_size, position, total_in_queue, status, estimated_wait_time)
VALUES ('TA-A1D4', 1, 3, 3, 3, 5, 'CALLED', 0);

INSERT INTO queues (ticket_number, establishment_id, user_id, party_size, position, total_in_queue, status, estimated_wait_time)
VALUES ('MC-B2E5', 2, 4, 2, 1, 3, 'WAITING', 15);

INSERT INTO queues (ticket_number, establishment_id, user_id, party_size, position, total_in_queue, status, estimated_wait_time)
VALUES ('CD-C3F6', 5, 1, 1, 1, 2, 'WAITING', 5);

INSERT INTO queues (ticket_number, establishment_id, user_id, party_size, position, total_in_queue, status, estimated_wait_time)
VALUES ('TA-A1G7', 1, 4, 2, 4, 5, 'WAITING', 30);

INSERT INTO queues (ticket_number, establishment_id, user_id, party_size, position, total_in_queue, status, estimated_wait_time)
VALUES ('BS-D4H8', 4, 5, 1, 1, 2, 'WAITING', 20);

-- ============================================
-- INSERT FAVORITES (11 total)
-- ============================================

-- Alice's favorites
INSERT INTO favorites (user_id, establishment_id, added_at)
VALUES (1, 1, NOW() - INTERVAL '5 days');

INSERT INTO favorites (user_id, establishment_id, added_at)
VALUES (1, 2, NOW() - INTERVAL '3 days');

-- Bob's favorites
INSERT INTO favorites (user_id, establishment_id, added_at)
VALUES (2, 1, NOW() - INTERVAL '4 days');

INSERT INTO favorites (user_id, establishment_id, added_at)
VALUES (2, 3, NOW() - INTERVAL '2 days');

-- Carol's favorites
INSERT INTO favorites (user_id, establishment_id, added_at)
VALUES (3, 2, NOW() - INTERVAL '6 days');

INSERT INTO favorites (user_id, establishment_id, added_at)
VALUES (3, 4, NOW() - INTERVAL '1 day');

-- Tony's favorites
INSERT INTO favorites (user_id, establishment_id, added_at)
VALUES (4, 2, NOW() - INTERVAL '7 days');

INSERT INTO favorites (user_id, establishment_id, added_at)
VALUES (4, 3, NOW() - INTERVAL '5 days');

-- Maria's favorites
INSERT INTO favorites (user_id, establishment_id, added_at)
VALUES (5, 1, NOW() - INTERVAL '8 days');

INSERT INTO favorites (user_id, establishment_id, added_at)
VALUES (5, 5, NOW() - INTERVAL '2 days');

-- Admin's favorites
INSERT INTO favorites (user_id, establishment_id, added_at)
VALUES (6, 1, NOW() - INTERVAL '10 days');

INSERT INTO favorites (user_id, establishment_id, added_at)
VALUES (6, 2, NOW() - INTERVAL '4 days');

INSERT INTO favorites (user_id, establishment_id, added_at)
VALUES (6, 5, NOW() - INTERVAL '3 days');

-- ============================================
-- INSERT NOTIFICATIONS (5 total)
-- ============================================

-- Notification 1: Queue Called for Alice
INSERT INTO notifications (user_id, ticket_id, establishment_id, title, message, type, is_read, created_at, read_at)
VALUES (1, 1, 1, 'You were called!', 'Your turn at Tony''s Restaurant! Please proceed to the counter.', 'QUEUE_CALLED', false, NOW(), NULL);

-- Notification 2: Queue Joined for Bob
INSERT INTO notifications (user_id, ticket_id, establishment_id, title, message, type, is_read, created_at, read_at)
VALUES (2, 2, 1, 'Queue joined successfully', 'You have joined the queue at Tony''s Restaurant. Your position is 2.', 'QUEUE_JOINED', true, NOW() - INTERVAL '2 hours', NOW() - INTERVAL '1 hour 55 minutes');

-- Notification 3: Position Update for Carol
INSERT INTO notifications (user_id, ticket_id, establishment_id, title, message, type, is_read, created_at, read_at)
VALUES (3, 3, 1, 'Position updated', 'Your position in the queue has changed to 3. Estimated wait time: 30 minutes.', 'POSITION_UPDATE', false, NOW() - INTERVAL '30 minutes', NULL);

-- Notification 4: Queue Called for Tony
INSERT INTO notifications (user_id, ticket_id, establishment_id, title, message, type, is_read, created_at, read_at)
VALUES (4, 4, 2, 'You were called!', 'Your turn at Maria''s Café! Please proceed to the counter.', 'QUEUE_CALLED', true, NOW() - INTERVAL '1 hour', NOW() - INTERVAL '55 minutes');

-- Notification 5: Unread notification for Alice
INSERT INTO notifications (user_id, ticket_id, establishment_id, title, message, type, is_read, created_at, read_at)
VALUES (1, 5, 5, 'Position update', 'You are now at position 1 in the queue.', 'POSITION_UPDATE', false, NOW() - INTERVAL '15 minutes', NULL);

-- =============================================================================
-- SUMMARY OF TEST DATA
-- =============================================================================
-- Users: 6 total (3 customers, 2 merchants, 1 admin)
-- Establishments: 5 total
-- Queues: 7 total
-- Favorites: 13 total
-- Notifications: 5 total (3 unread, 2 read)
-- =============================================================================

