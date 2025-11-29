package chill_logistics.user_server.domain.entity;

import jakarta.persistence.*;
import lib.entity.BaseEntity;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "p_delivery_admin")
public class DeliveryAdmin extends BaseEntity {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId    // User의 id를 그대로 PK로 사용
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "hub_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID hubId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_admin_type", nullable = false, length = 15)
    private DeliveryAdminType deliveryAdminType;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_possibility", nullable = false, length = 15)
    private DeliveryPossibility deliveryPossibility;

    @Column(name = "delivery_sequence_num", nullable = false)
    private int deliverySequenceNum;
}
