CREATE TABLE IF NOT EXISTS loan (
    id CHAR(36) PRIMARY KEY,
    customer JSON NOT NULL,
    status VARCHAR(30) NOT NULL,
    version INT NOT NULL,
    proposals JSON,
    type VARCHAR(20),
    amount FLOAT,
    tax FLOAT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS outbox_event (
    cod_outbox_event CHAR(36) PRIMARY KEY,
    des_aggregate_type VARCHAR(30) NOT NULL,
    identity CHAR(36) NOT NULL,
    type VARCHAR(50) NOT NULL,
    revision INT NOT NULL,
    payload JSON NOT NULL,
    dat_occurred TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    snapshot JSON NOT NULL
);