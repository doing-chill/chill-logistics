-- Hub --

CREATE TABLE IF NOT EXISTS p_hub (
                                     id              BINARY(16) PRIMARY KEY,
    name            VARCHAR(20) NOT NULL,
    hub_manager_id  BINARY(16) NOT NULL,

    postal_code     VARCHAR(20) NOT NULL,
    country         VARCHAR(50) NOT NULL,
    region          VARCHAR(50) NOT NULL,
    city            VARCHAR(50) NOT NULL,
    district        VARCHAR(50) NOT NULL,
    road_name       VARCHAR(100) NOT NULL,
    building_name   VARCHAR(50),
    detail_address  VARCHAR(100),
    full_address    VARCHAR(100) NOT NULL,

    latitude        DECIMAL(10,7) NOT NULL,
    longitude       DECIMAL(10,7) NOT NULL,

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BINARY(16) NOT NULL,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      BINARY(16) NOT NULL,
    deleted_at      TIMESTAMP NULL,
    deleted_by      BINARY(16)
    );

CREATE TABLE IF NOT EXISTS p_hub_info (
                                          id                  BINARY(16) PRIMARY KEY,
    start_hub_id        BINARY(16) NOT NULL,
    end_hub_id          BINARY(16) NOT NULL,
    delivery_duration   INT,
    distance            DECIMAL(10,3),

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BINARY(16),
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      BINARY(16),
    deleted_at      TIMESTAMP NULL,
    deleted_by      BINARY(16),

    CONSTRAINT fk_hubinfo_start_hub
    FOREIGN KEY (start_hub_id) REFERENCES p_hub(id),

    CONSTRAINT fk_hubinfo_end_hub
    FOREIGN KEY (end_hub_id) REFERENCES p_hub(id)
    );

CREATE TABLE IF NOT EXISTS p_hub_route_log (
                                               id              BINARY(16) PRIMARY KEY,
    start_hub_id    BINARY(16) NOT NULL,
    end_hub_id      BINARY(16) NOT NULL,
    total_duration  INT,
    total_distance  DECIMAL(10,3),

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BINARY(16),
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      BINARY(16),
    deleted_at      TIMESTAMP NULL,
    deleted_by      BINARY(16),

    CONSTRAINT fk_route_start_hub
    FOREIGN KEY (start_hub_id) REFERENCES p_hub(id),

    CONSTRAINT fk_route_end_hub
    FOREIGN KEY (end_hub_id) REFERENCES p_hub(id)
    );

CREATE TABLE IF NOT EXISTS p_hub_route_log_stop (
                                                    id                  BINARY(16) PRIMARY KEY,
    hub_id              BINARY(16) NOT NULL,
    hub_route_log_id    BINARY(16) NOT NULL,
    sequence_num        INT NOT NULL,

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BINARY(16),
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      BINARY(16),
    deleted_at      TIMESTAMP NULL,
    deleted_by      BINARY(16),

    CONSTRAINT fk_hub_route_stop_hub
    FOREIGN KEY (hub_id) REFERENCES p_hub(id),

    CONSTRAINT fk_hub_route_stop_route
    FOREIGN KEY (hub_route_log_id) REFERENCES p_hub_route_log(id)
    );

-- Delivery --

CREATE TABLE IF NOT EXISTS p_hub_delivery (
                                              id                          BINARY(16) PRIMARY KEY,
    order_id                    BINARY(16) NOT NULL,

    start_hub_id                BINARY(16) NOT NULL,
    start_hub_name              VARCHAR(20) NOT NULL,
    start_hub_full_address      VARCHAR(100) NOT NULL,

    end_hub_id                  BINARY(16) NOT NULL,
    end_hub_name                VARCHAR(20) NOT NULL,
    end_hub_full_address        VARCHAR(100) NOT NULL,

    delivery_person_id          BINARY(16),
    delivery_sequence_num       INT,

    delivery_status VARCHAR(15) NOT NULL
    CHECK (delivery_status IN ('WAITING','IN_PROGRESS','ARRIVED','COMPLETED','CANCELED')),

    expected_distance           DECIMAL(10,3),
    expected_delivery_duration  INT,
    distance                    DECIMAL(10,3),
    delivery_duration           INT,

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BINARY(16),
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      BINARY(16),
    deleted_at      TIMESTAMP NULL,
    deleted_by      BINARY(16)
    );


CREATE TABLE IF NOT EXISTS p_firm_delivery (
                                               id                          BINARY(16) PRIMARY KEY,
    order_id                    BINARY(16) NOT NULL,
    start_hub_id                BINARY(16) NOT NULL,
    receiver_firm_address_id    BINARY(16) NOT NULL,
    delivery_person_id          BINARY(16),
    delivery_sequence_num       INT,
    receiver_firm_owner_name    VARCHAR(15),

    delivery_status VARCHAR(15) NOT NULL
    CHECK (delivery_status IN ('WAITING','IN_PROGRESS','ARRIVED','COMPLETED','CANCELED')),

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BINARY(16),
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by      BINARY(16),
    deleted_at      TIMESTAMP NULL,
    deleted_by      BINARY(16)
    );
