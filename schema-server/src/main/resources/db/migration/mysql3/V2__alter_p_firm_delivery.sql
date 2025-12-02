ALTER TABLE p_firm_delivery
    CHANGE COLUMN receiver_firm_address_id receiver_firm_full_address VARCHAR(100) NOT NULL,
    CHANGE COLUMN start_hub_id end_hub_id BINARY(16) NOT NULL,
    ADD COLUMN receiver_firm_id BINARY(16) NOT NULL AFTER end_hub_id,
DROP CHECK p_firm_delivery_chk_1,
    ADD CONSTRAINT chk_delivery_status
        CHECK (delivery_status IN (
            'WAITING_FOR_HUB',
            'MOVING_TO_HUB',
            'ARRIVED_DESTINATION_HUB',
            'MOVING_TO_FIRM',
            'DELIVERY_IN_PROGRESS',
            'DELIVERY_COMPLETED',
            'DELIVERY_CANCELLED'
        ));