-- Order --

CREATE TABLE IF NOT EXISTS p_order (
                                       id                  BINARY(16) PRIMARY KEY,
    supplier_firm_id    BINARY(16) NOT NULL,
    receiver_firm_id    BINARY(16) NOT NULL,
    request_note        TEXT,

    order_status VARCHAR(15) NOT NULL
    CHECK (order_status IN ('CREATED','PROCESSING','IN_TRANSIT','COMPLETED','CANCELED')),

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BINARY(16) NOT NULL,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      BINARY(16) NOT NULL,
    deleted_at      TIMESTAMP NULL,
    deleted_by      BINARY(16)
    );

CREATE TABLE IF NOT EXISTS p_order_product (
                                               id              BINARY(16) PRIMARY KEY,
    order_id        BINARY(16) NOT NULL,
    product_id      BINARY(16) NOT NULL,
    product_name    VARCHAR(100) NOT NULL,
    product_price   INT NOT NULL,
    quantity        INT NOT NULL,

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BINARY(16) NOT NULL,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      BINARY(16) NOT NULL,
    deleted_at      TIMESTAMP NULL,
    deleted_by      BINARY(16),

    CONSTRAINT fk_orderproduct_order
    FOREIGN KEY (order_id) REFERENCES p_order(id)
    ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS p_order_query (
                                             id                          BINARY(16) PRIMARY KEY,
    supplier_firm_id            BINARY(16) NOT NULL,
    receiver_firm_id            BINARY(16) NOT NULL,
    receiver_firm_full_address  VARCHAR(100) NOT NULL,
    receiver_firm_owner_name    VARCHAR(15) NOT NULL,
    supplier_hub_id             BINARY(16) NOT NULL,
    receiver_hub_id             BINARY(16) NOT NULL,
    request_note                TEXT,

    order_status VARCHAR(15) NOT NULL
    CHECK (order_status IN ('CREATED','PROCESSING','IN_TRANSIT','COMPLETED','CANCELED')),

    product_name        VARCHAR(100) NOT NULL,
    product_quantity    INT NOT NULL,

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BINARY(16) NOT NULL,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      BINARY(16) NOT NULL,
    deleted_at      TIMESTAMP NULL,
    deleted_by      BINARY(16)
    );

-- Product --

CREATE TABLE IF NOT EXISTS p_product (
                                         id              BINARY(16) PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    firm_id         BINARY(16) NOT NULL,
    hub_id          BINARY(16) NOT NULL,
    stock_quantity  INT NOT NULL,
    price           INT NOT NULL,
    sellable        BOOLEAN NOT NULL DEFAULT TRUE,

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BINARY(16) NOT NULL,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      BINARY(16) NOT NULL,
    deleted_at      TIMESTAMP NULL,
    deleted_by      BINARY(16)
    );
