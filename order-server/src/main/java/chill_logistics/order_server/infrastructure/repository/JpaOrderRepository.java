package chill_logistics.order_server.infrastructure.repository;

import chill_logistics.order_server.domain.entity.Order;
import chill_logistics.order_server.domain.entity.OrderStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaOrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o WHERE (:supplierFirmId IS NULL OR o.supplierFirmId = :supplierFirmId) " +
            "AND (:receiverFirmId IS NULL OR o.receiverFirmId = :receiverFirmId) " +
            "AND (:orderStatus IS NULL OR o.orderStatus = :orderStatus)")
    List<Order> readOrderList(@Param("supplierFirmId") UUID supplierFirmId,
                              @Param("receiverFirmId") UUID receiverFirmId,
                              @Param("orderStatus") OrderStatus orderStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Order p where p.id = :orderId")
    Optional<Order> findByIdForUpdate(UUID orderId);
}
