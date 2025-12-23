ALTER TABLE p_order_outbox_event
DROP CONSTRAINT p_order_outbox_event_chk_1;

ALTER TABLE p_order_outbox_event
    ADD CONSTRAINT p_order_outbox_event_chk_1
        CHECK (order_outbox_status IN ('PENDING', 'PUBLISHED', 'FAILED'));
