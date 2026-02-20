-- Add merchant_id column to queues table
ALTER TABLE queues ADD COLUMN merchant_id BIGINT NOT NULL DEFAULT 1;

-- Create index for faster queries by merchant
CREATE INDEX idx_queues_merchant_id ON queues(merchant_id);

-- Create composite index for merchant and establishment queries
CREATE INDEX idx_queues_merchant_establishment ON queues(merchant_id, establishment_id);

-- Add foreign key constraint to users table (merchants are users with MERCHANT type)
ALTER TABLE queues ADD CONSTRAINT fk_queues_merchant_id
  FOREIGN KEY (merchant_id) REFERENCES users(id);

