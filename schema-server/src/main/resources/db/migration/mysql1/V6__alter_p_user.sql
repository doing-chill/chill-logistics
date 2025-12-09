ALTER TABLE p_user
DROP CHECK p_user_chk_1;

ALTER TABLE p_user
    ADD CONSTRAINT chk_p_user_role
        CHECK (role IN (
                        'MASTER',
                        'HUB_MANAGER',
                        'HUB_DELIVERY_MANAGER',
                        'FIRM_DELIVERY_MANAGER',
                        'FIRM_MANAGER'
            ));
