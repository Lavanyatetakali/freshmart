-- =====================================================
-- FreshMart Database Schema
-- MySQL 8.0+
-- =====================================================

CREATE DATABASE IF NOT EXISTS freshmart_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE freshmart_db;

-- =====================================================
-- TABLE: categories
-- =====================================================
CREATE TABLE IF NOT EXISTS categories (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    image_url   VARCHAR(500),
    active      BOOLEAN DEFAULT TRUE,
    INDEX idx_categories_active (active)
) ENGINE=InnoDB;

-- =====================================================
-- TABLE: users
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name   VARCHAR(100) NOT NULL,
    last_name    VARCHAR(100) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    phone        VARCHAR(20),
    role         ENUM('USER','ADMIN') DEFAULT 'USER',
    enabled      BOOLEAN DEFAULT TRUE,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_email  (email),
    INDEX idx_users_role   (role)
) ENGINE=InnoDB;

-- =====================================================
-- TABLE: products
-- =====================================================
CREATE TABLE IF NOT EXISTS products (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    price           DECIMAL(10,2) NOT NULL,
    old_price       DECIMAL(10,2),
    unit            VARCHAR(50),
    image_url       VARCHAR(500),
    emoji           VARCHAR(20),
    badge           VARCHAR(30),
    stock_quantity  INT NOT NULL DEFAULT 0,
    rating          DOUBLE DEFAULT 0.0,
    review_count    INT DEFAULT 0,
    active          BOOLEAN DEFAULT TRUE,
    is_new          BOOLEAN DEFAULT FALSE,
    category_id     BIGINT NOT NULL,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FULLTEXT INDEX ft_products_name (name),
    INDEX idx_products_category (category_id),
    INDEX idx_products_active   (active),
    INDEX idx_products_price    (price)
) ENGINE=InnoDB;

-- =====================================================
-- TABLE: cart_items
-- =====================================================
CREATE TABLE IF NOT EXISTS cart_items (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    quantity    INT NOT NULL DEFAULT 1,
    added_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)    REFERENCES users(id)    ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    UNIQUE KEY uq_cart_user_product (user_id, product_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABLE: orders
-- =====================================================
CREATE TABLE IF NOT EXISTS orders (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number     VARCHAR(50) NOT NULL UNIQUE,
    user_id          BIGINT NOT NULL,
    subtotal         DECIMAL(10,2) NOT NULL,
    delivery_charge  DECIMAL(10,2) DEFAULT 0.00,
    discount         DECIMAL(10,2) DEFAULT 0.00,
    total_amount     DECIMAL(10,2) NOT NULL,
    coupon_code      VARCHAR(50),
    status           ENUM('PENDING','CONFIRMED','PROCESSING','SHIPPED','DELIVERED','CANCELLED') DEFAULT 'PENDING',
    address_line1    VARCHAR(255),
    address_line2    VARCHAR(255),
    city             VARCHAR(100),
    state            VARCHAR(100),
    pin_code         VARCHAR(10),
    delivery_phone   VARCHAR(20),
    payment_method   VARCHAR(50),
    delivery_slot    VARCHAR(100),
    payment_status   ENUM('PENDING','PAID','FAILED','REFUNDED') DEFAULT 'PENDING',
    transaction_id   VARCHAR(100),
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    delivered_at     DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_orders_user   (user_id),
    INDEX idx_orders_status (status),
    INDEX idx_orders_date   (created_at)
) ENGINE=InnoDB;

-- =====================================================
-- TABLE: order_items
-- =====================================================
CREATE TABLE IF NOT EXISTS order_items (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id    BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    quantity    INT NOT NULL,
    unit_price  DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id)   REFERENCES orders(id)   ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_order_items_order (order_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABLE: coupons
-- =====================================================
CREATE TABLE IF NOT EXISTS coupons (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    code              VARCHAR(50) NOT NULL UNIQUE,
    discount_type     ENUM('PERCENTAGE','FLAT') NOT NULL,
    discount_value    DECIMAL(10,2) NOT NULL,
    min_order_amount  DECIMAL(10,2) DEFAULT 0.00,
    usage_limit       INT,
    used_count        INT DEFAULT 0,
    expiry_date       DATE,
    active            BOOLEAN DEFAULT TRUE,
    INDEX idx_coupons_code (code)
) ENGINE=InnoDB;

-- =====================================================
-- TABLE: payments
-- =====================================================
CREATE TABLE IF NOT EXISTS payments (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id          BIGINT NOT NULL UNIQUE,
    amount            DECIMAL(10,2) NOT NULL,
    method            VARCHAR(50),
    transaction_id    VARCHAR(100),
    gateway_response  TEXT,
    status            ENUM('PENDING','SUCCESS','FAILED','REFUNDED') DEFAULT 'PENDING',
    created_at        DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- SEED DATA
-- =====================================================

-- Categories
INSERT INTO categories (name, description, image_url) VALUES
('Vegetables', 'Fresh organic vegetables sourced daily from farms', '/images/cat-vegetables.jpg'),
('Fruits',     'Seasonal fresh fruits from certified orchards',      '/images/cat-fruits.jpg'),
('Dairy',      'Farm-fresh dairy products – milk, cheese, butter',   '/images/cat-dairy.jpg'),
('Snacks',     'Healthy and indulgent snacks for every craving',     '/images/cat-snacks.jpg'),
('Pulses',     'High-protein pulses, dals and legumes',              '/images/cat-pulses.jpg'),
('Beverages',  'Refreshing beverages – teas, coffees, juices',       '/images/cat-beverages.jpg');

-- Admin User (password: Admin@1234)
INSERT INTO users (first_name, last_name, email, password, phone, role) VALUES
('Admin', 'FreshMart', 'admin@freshmart.in',
 '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj2NJwrkm5jm',
 '+91 98765 00000', 'ADMIN');

-- Products – Vegetables
INSERT INTO products (name, description, price, old_price, unit, emoji, badge, stock_quantity, rating, review_count, category_id, is_new) VALUES
('Organic Tomatoes',  'Sun-ripened tomatoes from certified organic farms',              49.00, 69.00, '500g',  '🍅', 'SALE', 120, 4.5, 238, 1, false),
('Fresh Spinach',     'Tender baby spinach leaves, washed and ready to use',           39.00, NULL,  '250g',  '🥬', NULL,  85,  4.8, 192, 1, true),
('Broccoli Crown',    'Premium broccoli, hand-picked for maximum freshness',            79.00, 99.00, '500g',  '🥦', 'SALE', 60, 4.6, 145, 1, false),
('Baby Carrots',      'Sweet, tender baby carrots perfect for snacking',               55.00, NULL,  '400g',  '🥕', NULL,    5, 4.4,  87, 1, false),
('Sweet Corn',        'Fresh golden sweet corn on the cob',                            35.00, NULL,  '2 pcs', '🌽', NULL,  200, 4.7, 312, 1, true),
('Capsicum Mix',      'Colourful trio of red, yellow and green capsicums',             89.00, 120.00,'500g',  '🫑', 'SALE', 40, 4.3,  76, 1, false),
('Eggplant',          'Glossy purple brinjal, ideal for curries and grills',           45.00, NULL,  '500g',  '🍆', NULL,   70, 4.2,  54, 1, false),
('Cucumber',          'Cool, crisp cucumbers fresh from the farm',                     29.00, NULL,  '500g',  '🥒', NULL,  150, 4.5, 198, 1, false);

-- Products – Fruits
INSERT INTO products (name, description, price, old_price, unit, emoji, badge, stock_quantity, rating, review_count, category_id, is_new) VALUES
('Royal Bananas',     'Ripe Robusta bananas, naturally sweet',                         59.00, NULL,  '1 dozen','🍌',NULL,  300, 4.9, 567, 2, false),
('Kashmiri Apples',   'Large, crispy Fuji apples from the Kashmir valley',            149.00,189.00,'1 kg',  '🍎','SALE', 80, 4.8, 423, 2, false),
('Sweet Oranges',     'Juicy Nagpur oranges, vitamin C-packed',                        99.00, NULL,  '1 kg',  '🍊', NULL,  110, 4.7, 278, 2, true),
('Alphonso Mangoes',  'The king of mangoes – premium Devgad Alphonso',               299.00, 399.00,'1 kg',  '🥭', 'HOT', 45, 5.0, 892, 2, false),
('Watermelon',        'Seedless watermelon, refreshingly sweet',                      199.00, NULL,  '2-3 kg','🍉', NULL,   30, 4.6, 167, 2, false),
('Strawberries',      'Plump, fragrant strawberries from Mahabaleshwar',             179.00, 229.00,'250g',  '🍓','SALE', 25, 4.8, 334, 2, false),
('Grapes',            'Seedless green Thompson grapes, firm and sweet',               129.00, NULL,  '500g',  '🍇', NULL,   90, 4.5, 201, 2, true),
('Pomegranate',       'Arils-full pomegranate, antioxidant-rich',                     119.00,149.00,'500g',  '🍎','SALE',  3, 4.7, 156, 2, false);

-- Products – Dairy
INSERT INTO products (name, description, price, old_price, unit, emoji, badge, stock_quantity, rating, review_count, category_id, is_new) VALUES
('Full Cream Milk',   'Fresh pasteurised full-cream milk, delivered daily',           62.00, NULL,  '1 Liter','🥛', NULL,  500, 4.8, 892, 3, false),
('Amul Butter',       'Rich, creamy salted butter – a household staple',              58.00, NULL,  '100g',  '🧈', NULL,  200, 4.9,1234, 3, false),
('Greek Yogurt',      'Thick, protein-rich Greek-style yogurt',                       89.00, 109.00,'400g',  '🍦','SALE', 120, 4.7, 445, 3, false),
('Paneer',            'Soft, fresh cottage cheese made from full-cream milk',         89.00, NULL,  '200g',  '🧀', NULL,   80, 4.8, 678, 3, false),
('Cheddar Cheese',    'Aged sharp cheddar, great for sandwiches and melting',        149.00, 189.00,'200g',  '🧀','SALE', 60, 4.6, 234, 3, true),
('Dahi (Curd)',        'Smooth set curd, mildly tangy and probiotic-rich',             45.00, NULL,  '500g',  '🥣', NULL,  300, 4.7, 567, 3, false);

-- Products – Snacks
INSERT INTO products (name, description, price, old_price, unit, emoji, badge, stock_quantity, rating, review_count, category_id, is_new) VALUES
('Oat Cookies',       'Baked whole-oat cookies with no artificial preservatives',     79.00, 99.00, '200g',  '🍪','SALE', 150, 4.5, 312, 4, false),
('Mixed Nuts Trail',  'Premium mix of almonds, cashews, walnuts and raisins',        249.00, NULL,  '300g',  '🥜', NULL,   70, 4.8, 456, 4, false),
('Whole Wheat Bread', 'Freshly baked 100% whole-wheat sandwich bread',               55.00, NULL,  '400g',  '🍞', NULL,  200, 4.6, 789, 4, true),
('Potato Chips',      'Thinly sliced, lightly salted crispy potato chips',            35.00, 45.00, '100g',  '🥔','SALE', 400, 4.4,1023, 4, false),
('Dark Chocolate',    '70% cocoa dark chocolate – rich and indulgent',              129.00, NULL,  '100g',  '🍫', NULL,   90, 4.9, 567, 4, false),
('Caramel Popcorn',   'Crunchy caramel-glazed popcorn in a resealable pack',         89.00, 109.00,'200g',  '🍿','SALE', 120, 4.5, 234, 4, false);

-- Products – Pulses
INSERT INTO products (name, description, price, old_price, unit, emoji, badge, stock_quantity, rating, review_count, category_id, is_new) VALUES
('Toor Dal',          'Premium arhar/toor dal, ideal for sambar and dal tadka',     149.00, NULL,  '1 kg',  '🫘', NULL,  200, 4.7, 445, 5, false),
('Moong Dal',         'Hulled yellow moong dal, light and easy to digest',          129.00, NULL,  '1 kg',  '🫘', NULL,  180, 4.8, 334, 5, false),
('Chana Dal',         'Hearty Bengal gram dal, rich in protein and fibre',           99.00, 120.00,'1 kg',  '🫘','SALE', 220, 4.6, 278, 5, false),
('Masoor Dal',        'Red lentils – quick-cooking and nutritious',                  89.00, NULL,  '1 kg',  '🫘', NULL,  160, 4.5, 212, 5, true),
('Rajma',             'Dark kidney beans for the classic Punjabi rajma-chawal',    139.00, 169.00,'1 kg',  '🫘','SALE', 100, 4.8, 398, 5, false),
('Black Chana',       'Whole black chickpeas, chewy and flavour-packed',            119.00, NULL,  '1 kg',  '🫘', NULL,  140, 4.7, 267, 5, false);

-- Products – Beverages
INSERT INTO products (name, description, price, old_price, unit, emoji, badge, stock_quantity, rating, review_count, category_id, is_new) VALUES
('Green Tea',         '25-bag pack of premium Darjeeling green tea',               149.00, 189.00,'25 bags','🍵','SALE', 200, 4.8, 654, 6, false),
('Cold Brew Coffee',  'Smooth, low-acid cold-brew concentrate',                    299.00, NULL,  '250ml', '☕', NULL,   60, 4.9, 345, 6, true),
('Mango Juice',       '100% natural Alfonso mango juice – no added sugar',          49.00, 65.00, '200ml', '🥤','SALE', 350, 4.6, 892, 6, false),
('Coconut Water',     'Fresh young coconut water – naturally hydrating',            45.00, NULL,  '250ml', '🥥', NULL,  180, 4.7, 456, 6, false);

-- Coupons
INSERT INTO coupons (code, discount_type, discount_value, min_order_amount, usage_limit, active) VALUES
('FRESHMART20', 'PERCENTAGE', 20.00, 299.00, 10000, TRUE),
('SAVE50',      'FLAT',       50.00, 499.00,  5000, TRUE),
('NEWUSER10',   'PERCENTAGE', 10.00,   0.00, 99999, TRUE),
('DAIRY15',     'PERCENTAGE', 15.00, 150.00,  2000, TRUE);

