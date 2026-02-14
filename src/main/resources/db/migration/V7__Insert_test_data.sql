-- V7__Insert_test_data.sql
-- Test data for Filae API - Sprint 1

-- Clear existing data (if any)
DELETE FROM queues;
DELETE FROM establishments;
DELETE FROM users;

-- Reset sequences
ALTER SEQUENCE users_id_seq RESTART WITH 1;
ALTER SEQUENCE establishments_id_seq RESTART WITH 1;
ALTER SEQUENCE queues_id_seq RESTART WITH 1;

-- ============================================
-- INSERT TEST USERS
-- ============================================
-- All passwords are hashed with BCrypt
-- Hashes generated from actual Spring Security BCryptPasswordEncoder

-- User 1: Customer
-- Password: SecurePass123!
-- Hash generated from actual BCrypt encoding
INSERT INTO users (name, email, password_hash, phone, user_type, is_active)
VALUES ('Alice Silva', 'alice@example.com', '$2a$10$3SFNPU2z7Jt3AQI6K.1s2ey4R1.5ymSBPqGtqJLSxPk2EsKFteFd2', '11998765432', 'CUSTOMER', TRUE);

-- User 2: Customer
-- Password: SecurePass123!
INSERT INTO users (name, email, password_hash, phone, user_type, is_active)
VALUES ('Bob Santos', 'bob@example.com', '$2a$10$3SFNPU2z7Jt3AQI6K.1s2ey4R1.5ymSBPqGtqJLSxPk2EsKFteFd2', '11987654321', 'CUSTOMER', TRUE);

-- User 3: Customer
-- Password: SecurePass123!
INSERT INTO users (name, email, password_hash, phone, user_type, is_active)
VALUES ('Carol Oliveira', 'carol@example.com', '$2a$10$3SFNPU2z7Jt3AQI6K.1s2ey4R1.5ymSBPqGtqJLSxPk2EsKFteFd2', '11976543210', 'CUSTOMER', TRUE);

-- User 4: Merchant
-- Password: SecurePass123!
INSERT INTO users (name, email, password_hash, phone, user_type, is_active)
VALUES ('Tony Rossi', 'tony@tonysrestaurant.com', '$2a$10$3SFNPU2z7Jt3AQI6K.1s2ey4R1.5ymSBPqGtqJLSxPk2EsKFteFd2', '1133334444', 'MERCHANT', TRUE);

-- User 5: Merchant
-- Password: SecurePass123!
INSERT INTO users (name, email, password_hash, phone, user_type, is_active)
VALUES ('Maria Café', 'maria@mariacafe.com', '$2a$10$3SFNPU2z7Jt3AQI6K.1s2ey4R1.5ymSBPqGtqJLSxPk2EsKFteFd2', '1144445555', 'MERCHANT', TRUE);

-- User 6: Admin
-- Password: SecurePass123!
INSERT INTO users (name, email, password_hash, phone, user_type, is_active)
VALUES ('System Admin', 'admin@filae.com', '$2a$10$3SFNPU2z7Jt3AQI6K.1s2ey4R1.5ymSBPqGtqJLSxPk2EsKFteFd2', '1155556666', 'ADMIN', TRUE);

-- ============================================
-- INSERT TEST ESTABLISHMENTS
-- ============================================

-- Establishment 1: Tony's Restaurant
INSERT INTO establishments
(name, description, category, address, city, state, zip_code, phone_number, email,
 is_accepting_customers, queue_enabled, merchant_id, rating, review_count)
VALUES
('Tony''s Restaurant', 'Italian cuisine with traditional recipes from Naples', 'restaurant',
 'Rua Augusta, 1500', 'São Paulo', 'SP', '01305-100', '1133334444', 'tony@tonysrestaurant.com',
 TRUE, TRUE, 4, 4.8, 156);

-- Establishment 2: Maria's Café
INSERT INTO establishments
(name, description, category, address, city, state, zip_code, phone_number, email,
 is_accepting_customers, queue_enabled, merchant_id, rating, review_count)
VALUES
('Maria''s Café', 'Cozy café with specialty coffee and pastries', 'café',
 'Av. Paulista, 800', 'São Paulo', 'SP', '01311-100', '1144445555', 'maria@mariacafe.com',
 TRUE, TRUE, 5, 4.6, 89);

-- Establishment 3: Fast Burgers
INSERT INTO establishments
(name, description, category, address, city, state, zip_code, phone_number, email,
 is_accepting_customers, queue_enabled, merchant_id, rating, review_count)
VALUES
('Fast Burgers', 'Premium burgers and craft beers', 'fast food',
 'Rua Oscar Freire, 500', 'São Paulo', 'SP', '01426-100', '1155557777', 'contact@fastburgers.com',
 TRUE, TRUE, 4, 4.3, 234);

-- Establishment 4: Barber Shop Plus
INSERT INTO establishments
(name, description, category, address, city, state, zip_code, phone_number, email,
 is_accepting_customers, queue_enabled, merchant_id, rating, review_count)
VALUES
('Barber Shop Plus', 'Modern barbershop with experienced stylists', 'barbershop',
 'Rua Consolação, 2000', 'São Paulo', 'SP', '01302-100', '1166668888', 'barbershop@plus.com',
 TRUE, TRUE, 5, 4.9, 312);

-- Establishment 5: Cinema Downtown
INSERT INTO establishments
(name, description, category, address, city, state, zip_code, phone_number, email,
 is_accepting_customers, queue_enabled, merchant_id, rating, review_count)
VALUES
('Cinema Downtown', 'Modern cinema with 8 screens and premium sound', 'cinema',
 'Av. Rio Branco, 1200', 'São Paulo', 'SP', '01401-100', '1177779999', 'info@cinemadowntown.com',
 TRUE, TRUE, 4, 4.7, 445);

-- ============================================
-- INSERT TEST QUEUE ENTRIES
-- ============================================

-- Queue 1: Alice waiting at Tony's Restaurant
INSERT INTO queues
(ticket_number, establishment_id, user_id, party_size, notes, position, total_in_queue,
 status, estimated_wait_time, joined_at)
VALUES
('TA-A1B2', 1, 1, 2, 'Window seat preferred', 1, 5, 'WAITING', 50, NOW() - INTERVAL '30 minutes');

-- Queue 2: Bob waiting at Tony's Restaurant
INSERT INTO queues
(ticket_number, establishment_id, user_id, party_size, notes, position, total_in_queue,
 status, estimated_wait_time, joined_at)
VALUES
('TA-C3D4', 1, 2, 4, 'Special occasion - birthday', 2, 5, 'WAITING', 60, NOW() - INTERVAL '20 minutes');

-- Queue 3: Carol called at Tony's Restaurant
INSERT INTO queues
(ticket_number, establishment_id, user_id, party_size, notes, position, total_in_queue,
 status, estimated_wait_time, joined_at, called_at)
VALUES
('TA-E5F6', 1, 3, 1, 'Vegetarian options', 0, 5, 'CALLED', 0, NOW() - INTERVAL '40 minutes', NOW() - INTERVAL '5 minutes');

-- Queue 4: Alice waiting at Maria's Café
INSERT INTO queues
(ticket_number, establishment_id, user_id, party_size, notes, position, total_in_queue,
 status, estimated_wait_time, joined_at)
VALUES
('MC-G7H8', 2, 1, 1, 'Cappuccino to go', 1, 3, 'WAITING', 30, NOW() - INTERVAL '15 minutes');

-- Queue 5: Bob waiting at Fast Burgers
INSERT INTO queues
(ticket_number, establishment_id, user_id, party_size, notes, position, total_in_queue,
 status, estimated_wait_time, joined_at)
VALUES
('FB-I9J0', 3, 2, 2, 'Extra cheese and bacon', 3, 7, 'WAITING', 70, NOW() - INTERVAL '10 minutes');

-- Queue 6: Carol finished at Barber Shop
INSERT INTO queues
(ticket_number, establishment_id, user_id, party_size, notes, position, total_in_queue,
 status, estimated_wait_time, joined_at, called_at, finished_at)
VALUES
('BS-K1L2', 4, 3, 1, 'Beard trim and styling', 0, 1, 'FINISHED', 0, NOW() - INTERVAL '60 minutes', NOW() - INTERVAL '50 minutes', NOW() - INTERVAL '5 minutes');

-- Queue 7: Test multiple people in queue at Cinema
INSERT INTO queues
(ticket_number, establishment_id, user_id, party_size, notes, position, total_in_queue,
 status, estimated_wait_time, joined_at)
VALUES
('CD-M3N4', 5, 1, 2, 'Dune Part Two - 7 PM show', 1, 4, 'WAITING', 40, NOW() - INTERVAL '25 minutes');

-- Queue 8: Test multiple people in queue at Cinema
INSERT INTO queues
(ticket_number, establishment_id, user_id, party_size, notes, position, total_in_queue,
 status, estimated_wait_time, joined_at)
VALUES
('CD-O5P6', 5, 2, 3, 'Family package', 2, 4, 'WAITING', 50, NOW() - INTERVAL '18 minutes');

-- Queue 9: Test multiple people in queue at Cinema
INSERT INTO queues
(ticket_number, establishment_id, user_id, party_size, notes, position, total_in_queue,
 status, estimated_wait_time, joined_at)
VALUES
('CD-Q7R8', 5, 3, 1, 'Student ticket', 3, 4, 'WAITING', 60, NOW() - INTERVAL '8 minutes');

-- ============================================
-- SUMMARY OF TEST DATA
-- ============================================
-- Users:
--   - 3 Customers (Alice, Bob, Carol)
--   - 2 Merchants (Tony, Maria)
--   - 1 Admin (System Admin)
--
-- Establishments:
--   - 5 establishments across different categories
--   - Ready to manage queues
--
-- Queues:
--   - 9 queue entries across different establishments
--   - Various statuses: WAITING, CALLED, FINISHED
--   - Multiple users in same queue
--
-- Testing the following scenarios:
--   1. Login with test credentials
--   2. Browse establishments
--   3. Filter establishments by category/city
--   4. View queue status
--   5. Test position management
--   6. Test state transitions

