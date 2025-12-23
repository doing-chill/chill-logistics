ALTER TABLE p_order_product
    ADD COLUMN stock_status VARCHAR(50) NOT NULL
        DEFAULT 'STOCK_CONFIRMED'
        CONSTRAINT chk_stock_status
            CHECK (stock_status IN (
                                    'STOCK_PENDING',
                                    'STOCK_CONFIRMED',
                                    'STOCK_FAILED'))
    AFTER quantity;
