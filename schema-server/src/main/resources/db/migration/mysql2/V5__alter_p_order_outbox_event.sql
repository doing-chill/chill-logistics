ALTER TABLE p_order_outbox_event
    ADD COLUMN published_at TIMESTAMP,
    ADD COLUMN last_retry_at TIMESTAMP;