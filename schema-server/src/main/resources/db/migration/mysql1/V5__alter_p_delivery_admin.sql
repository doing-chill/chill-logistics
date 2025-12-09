ALTER TABLE p_delivery_admin
DROP FOREIGN KEY fk_deliveryadmin_user;

ALTER TABLE p_delivery_admin
    ADD COLUMN user_id BINARY(16) NOT NULL AFTER id;

ALTER TABLE p_delivery_admin
DROP COLUMN delivery_admin_type;
