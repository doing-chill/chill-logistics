package chill_logistics.order_server.infrastructure.repository;

import chill_logistics.order_server.domain.entity.OrderOutboxEvent;
import chill_logistics.order_server.domain.entity.OrderOutboxStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaOrderOutboxEventRepository extends JpaRepository<OrderOutboxEvent, UUID> {

    @Query("SELECT e FROM OrderOutboxEvent e " +
           "WHERE e.orderOutboxStatus = :status " +
           "ORDER BY e.createdAt ASC")
    List<OrderOutboxEvent> findPendingEvents(
        @Param("status") OrderOutboxStatus status,
        Pageable pageable);

    /* 검색/관리용 (status/orderId 조건 검색) */
    @Query("SELECT e FROM OrderOutboxEvent e " +
           "WHERE (:status IS NULL OR e.orderOutboxStatus = :status) " +
           "AND (:orderId IS NULL OR e.orderId = :orderId) " +
           "ORDER BY e.createdAt ASC")
    List<OrderOutboxEvent> findOutboxEvents(
        @Param("status") OrderOutboxStatus status,
        @Param("orderId") UUID orderId,
        Pageable pageable);

    /* FAILED 모니터링/조회용 */
    @Query("SELECT e FROM OrderOutboxEvent e " +
           "WHERE e.orderOutboxStatus = 'FAILED' " +
           "ORDER BY e.lastRetryAt DESC, e.createdAt DESC")
    List<OrderOutboxEvent> findFailedEvents(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from OrderOutboxEvent e where e.id = :orderId")
    Optional<OrderOutboxEvent> findByIdForUpdate(UUID orderId);
}
