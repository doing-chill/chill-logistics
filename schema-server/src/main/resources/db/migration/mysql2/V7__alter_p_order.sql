ALTER TABLE p_order
DROP CHECK p_order_chk_1;

ALTER TABLE p_order
    ADD CONSTRAINT chk_p_order_status
        CHECK (order_status IN (
                        'CREATED',
                        'STOCK_PROCESSING',
                        'STOCK_CONFIRMED',
                        'PROCESSING',
                        'IN_TRANSIT',
                        'COMPLETED',
                        'CANCELED'
            ));
