ALTER TABLE p_hub_delivery
DROP CHECK p_hub_delivery_chk_1;

ALTER TABLE p_hub_delivery
    ADD CONSTRAINT chk_p_hub_delivery_status_v2
        CHECK (delivery_status IN (
                                   'WAITING_FOR_HUB',
                                   'MOVING_TO_HUB',
                                   'ARRIVED_DESTINATION_HUB',
                                   'MOVING_TO_FIRM',
                                   'DELIVERY_IN_PROGRESS',
                                   'DELIVERY_COMPLETED',
                                   'DELIVERY_CANCELLED'
            ));
