ALTER TABLE p_order_query
    MODIFY COLUMN order_status VARCHAR(50) NOT NULL;

ALTER TABLE p_order_query
DROP CHECK p_order_query_chk_1;

ALTER TABLE p_order_query
    ADD CONSTRAINT p_order_query_chk_1
        CHECK (order_status IN (
                                'CREATED',
                                'STOCK_PROCESSING',
                                'STOCK_CONFIRMED',
                                'PROCESSING',
                                'IN_TRANSIT',
                                'COMPLETED',
                                'CANCELED'
            ));
