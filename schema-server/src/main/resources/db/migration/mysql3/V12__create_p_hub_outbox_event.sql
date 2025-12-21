CREATE TABLE IF NOT EXISTS p_hub_outbox_event (
    id BINARY(16) PRIMARY KEY,
    order_id BINARY(16) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    hub_outbox_status VARCHAR(30) NOT NULL
    CHECK (hub_outbox_status IN ('INIT', 'PUBLISHED', 'FAILED')),
    retry_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);