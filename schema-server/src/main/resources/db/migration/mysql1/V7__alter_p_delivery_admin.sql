ALTER TABLE p_delivery_admin
    ADD COLUMN delivery_admin_type VARCHAR(15) NOT NULL
        CONSTRAINT chk_delivery_admin_type
            CHECK (delivery_admin_type IN ('HUB', 'FIRM'))
    AFTER hub_id;