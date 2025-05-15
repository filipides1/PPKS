CREATE DATABASE auction_db;

-- User table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL DEFAULT '$2a$10$eDIl8oNcfEGxQ7zAzwzKXuUP7v8YZV2K6YKXkQDqaG1qn.61BdHri'
    --default password: password
);

-- Auction item
CREATE TABLE auction_items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    starting_price NUMERIC(10, 2) NOT NULL,
    current_price NUMERIC(10, 2),
    winner_username VARCHAR(255),
    is_completed BOOLEAN DEFAULT false
);

-- Create bids table
CREATE TABLE bids (
    id SERIAL PRIMARY KEY,
    amount DOUBLE PRECISION NOT NULL,
    username VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    auction_item_id BIGINT NOT NULL REFERENCES auction_items(id)
);

-- Create chat_messages table
CREATE TABLE chat_messages (
    id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    username VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    auction_item_id BIGINT NOT NULL REFERENCES auction_items(id)
);

-- Favorites table
CREATE TABLE favorites (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    auction_item_id INT REFERENCES auction_items(id),
    UNIQUE (user_id, auction_item_id)
);

