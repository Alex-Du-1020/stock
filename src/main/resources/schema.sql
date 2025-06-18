-- Drop tables if they exist
DROP TABLE IF EXISTS SECURITY_QUANTITY;
DROP TABLE IF EXISTS SECURITY;
DROP TABLE IF EXISTS STOCK;

-- Create STOCK table
CREATE TABLE STOCK (
    id VARCHAR(50) PRIMARY KEY
);

-- Insert initial stock records
INSERT INTO STOCK (id) VALUES ('AAPL');
INSERT INTO STOCK (id) VALUES ('TELSA');
INSERT INTO STOCK (id) VALUES ('MSFT');

-- Create SECURITY table
CREATE TABLE SECURITY (
    ticker VARCHAR(50) PRIMARY KEY,
    stock_id VARCHAR(50) NOT NULL,
    type VARCHAR(20) NOT NULL,
    maturity VARCHAR(20),
    strike DOUBLE,
    FOREIGN KEY (stock_id) REFERENCES STOCK(id)
);

-- Insert SECURITY records based on position.csv
-- AAPL stock
INSERT INTO SECURITY (ticker, stock_id, type, maturity, strike) VALUES ('AAPL', 'AAPL', 'STOCK', NULL, NULL);

-- AAPL options
INSERT INTO SECURITY (ticker, stock_id, type, maturity, strike) VALUES ('AAPL-OCT-2020-110-C', 'AAPL', 'CALL', '2020-10-15', 0.05);
INSERT INTO SECURITY (ticker, stock_id, type, maturity, strike) VALUES ('AAPL-OCT-2020-110-P', 'AAPL', 'PUT', '2020-10-15', 0.005);

-- TELSA stock
INSERT INTO SECURITY (ticker, stock_id, type, maturity, strike) VALUES ('TELSA', 'TELSA', 'STOCK', NULL, NULL);

-- TELSA options
INSERT INTO SECURITY (ticker, stock_id, type, maturity, strike) VALUES ('TELSA-NOV-2020-400-C', 'TELSA', 'CALL', '2020-11-20', 0.06);
INSERT INTO SECURITY (ticker, stock_id, type, maturity, strike) VALUES ('TELSA-DEC-2020-400-P', 'TELSA', 'PUT', '2020-12-18', 0.016);

-- Create SECURITY_QUANTITY table
CREATE TABLE SECURITY_QUANTITY (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticker VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticker) REFERENCES SECURITY(ticker)
);


