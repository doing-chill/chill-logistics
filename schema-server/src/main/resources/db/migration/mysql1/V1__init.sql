-- User --

CREATE TABLE IF NOT EXISTS p_user (
                                      id              BINARY(16) PRIMARY KEY,

    email           VARCHAR(100) NOT NULL UNIQUE,
    username        VARCHAR(15) NOT NULL,
    nickname        VARCHAR(20) NOT NULL,
    password        VARCHAR(255) NOT NULL,

    role            VARCHAR(20) NOT NULL
    CHECK (role IN ('MASTER', 'HUB', 'DELIVERY', 'FIRM')),

    signup_status   VARCHAR(20) NOT NULL
    CHECK (signup_status IN ('PENDING', 'APPROVED', 'REJECTED')),

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BINARY(16) NOT NULL,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      BINARY(16) NOT NULL,
    deleted_at      TIMESTAMP NULL,
    deleted_by      BINARY(16)
    );

CREATE TABLE IF NOT EXISTS p_delivery_admin (
                                                id                      BINARY(16) PRIMARY KEY,
    hub_id                  BINARY(16) NOT NULL,

    delivery_admin_type     VARCHAR(15) NOT NULL
    CHECK (delivery_admin_type IN ('HUB', 'FIRM')),

    delivery_possibility    VARCHAR(15) NOT NULL
    CHECK (delivery_possibility IN ('POSSIBLE', 'IMPOSSIBLE')),

    delivery_sequence_num   INT NOT NULL,

    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by              BINARY(16) NOT NULL,
    updated_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by              BINARY(16) NOT NULL,
    deleted_at              TIMESTAMP NULL,
    deleted_by              BINARY(16),

    CONSTRAINT fk_deliveryadmin_user
    FOREIGN KEY (id) REFERENCES p_user(id)
    ON DELETE CASCADE
    );

-- Firm --

CREATE TABLE IF NOT EXISTS p_firm (
                                      id              BINARY(16) PRIMARY KEY,

    name            VARCHAR(50) NOT NULL,
    hub_id          BINARY(16) NOT NULL,
    owner_id        BINARY(16) NOT NULL,
    owner_name      VARCHAR(15) NOT NULL,

    firm_type       VARCHAR(15) NOT NULL
    CHECK (firm_type IN ('SUPPLIER', 'RECEIVER')),

    postal_code     VARCHAR(20) NOT NULL,
    country         VARCHAR(50) NOT NULL,
    region          VARCHAR(50) NOT NULL,
    city            VARCHAR(50) NOT NULL,
    district        VARCHAR(50) NOT NULL,
    road_name       VARCHAR(100) NOT NULL,
    building_name   VARCHAR(100),
    detail_address  VARCHAR(100),
    full_address    TEXT NOT NULL,

    latitude        DECIMAL(10,7) NOT NULL,
    longitude       DECIMAL(10,7) NOT NULL,

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BINARY(16) NOT NULL,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      BINARY(16) NOT NULL,
    deleted_at      TIMESTAMP NULL,
    deleted_by      BINARY(16)
    );
