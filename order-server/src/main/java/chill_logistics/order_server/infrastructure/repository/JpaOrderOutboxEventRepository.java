package chill_logistics.order_server.infrastructure.repository;

import chill_logistics.order_server.domain.entity.OrderOutboxEvent;
import chill_logistics.order_server.domain.entity.OrderOutboxStatus;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaOrderOutboxEventRepository extends JpaRepository<OrderOutboxEvent, UUID> {

    /* 처리 대상으로 가져와서 락 거는 목적 용도 (PENDING + Lock + 오래된 순) */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM OrderOutboxEvent e " +
           "WHERE e.orderOutboxStatus = :status " +
           "AND e.deletedAt IS NULL " +
           "ORDER BY e.createdAt ASC")
    List<OrderOutboxEvent> findPendingEventsForUpdate(
        @Param("status") OrderOutboxStatus status,
        Pageable pageable);

    /* 검색/관리용 (status/orderId 조건 검색) */
    @Query("SELECT e FROM OrderOutboxEvent e " +
           "WHERE (:status IS NULL OR e.orderOutboxStatus = :status) " +
           "AND (:orderId IS NULL OR e.orderId = :orderId) " +
           "AND e.deletedAt IS NULL " +
           "ORDER BY e.createdAt ASC")
    List<OrderOutboxEvent> findOutboxEvents(
        @Param("status") OrderOutboxStatus status,
        @Param("orderId") UUID orderId,
        Pageable pageable);

    /* FAILED 모니터링/조회용 */
    @Query("SELECT e FROM OrderOutboxEvent e " +
           "WHERE e.orderOutboxStatus = 'FAILED' " +
           "AND e.deletedAt IS NULL " +
           "ORDER BY e.lastRetryAt DESC, e.createdAt DESC")
    List<OrderOutboxEvent> findFailedEvents(Pageable pageable);
}
