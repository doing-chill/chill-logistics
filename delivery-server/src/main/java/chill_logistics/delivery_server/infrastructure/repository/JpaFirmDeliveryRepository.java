package chill_logistics.delivery_server.infrastructure.repository;

import chill_logistics.delivery_server.domain.entity.FirmDelivery;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFirmDeliveryRepository extends JpaRepository<FirmDelivery, UUID> {

    Page<FirmDelivery> findByDeletedAtIsNull(Pageable pageable);

    Page<FirmDelivery> findByReceiverFirmOwnerNameAndDeletedAtIsNull(String receiverFirmOwnerName, Pageable pageable);
}
