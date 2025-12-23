ALTER TABLE p_hub_outbox_event
DROP CONSTRAINT p_hub_outbox_event_chk_1;

ALTER TABLE p_hub_outbox_event
    ADD CONSTRAINT p_hub_outbox_event_chk_1
        CHECK (hub_outbox_status IN ('PENDING', 'PUBLISHED', 'FAILED'));
