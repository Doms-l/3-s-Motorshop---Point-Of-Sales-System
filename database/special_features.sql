-- 3'S Motor Shop POS: customer-focused feature tables
-- Select threes_motorshop_pos before running this script.
USE threes_motorshop_pos;

CREATE TABLE IF NOT EXISTS loyalty_customers (
  loyalty_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(150) NOT NULL,
  phone VARCHAR(30) NOT NULL UNIQUE,
  points INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS loyalty_point_history (
  history_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  loyalty_id INT NOT NULL,
  action_type VARCHAR(20) NOT NULL,
  points INT NOT NULL,
  note VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_loyalty_history_customer (loyalty_id),
  CONSTRAINT fk_loyalty_history_customer FOREIGN KEY (loyalty_id) REFERENCES loyalty_customers(loyalty_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS product_reservations (
  reservation_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  customer_name VARCHAR(150) NOT NULL,
  phone VARCHAR(30) NOT NULL,
  product_id INT NOT NULL,
  quantity INT NOT NULL,
  pickup_date DATE NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'Reserved',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_reservations_product (product_id),
  INDEX idx_reservations_status (status),
  CONSTRAINT fk_reservation_product FOREIGN KEY (product_id) REFERENCES products(product_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sales_returns (
  return_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  transaction_id INT NOT NULL,
  product_id INT NOT NULL,
  quantity INT NOT NULL,
  reason VARCHAR(255) NOT NULL,
  warranty_end DATE NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'Recorded',
  restocked TINYINT(1) NOT NULL DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_returns_transaction (transaction_id),
  INDEX idx_returns_product (product_id),
  CONSTRAINT fk_return_transaction FOREIGN KEY (transaction_id) REFERENCES sales_transactions(transaction_id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_return_product FOREIGN KEY (product_id) REFERENCES products(product_id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;
