package chill_logistics.user_server.domain.entity;

import jakarta.persistence.*;
import lib.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Entity
@Table(name = "p_delivery_admin")
public class DeliveryAdmin extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuidv7")
    @GenericGenerator(
            name = "uuidv7",
            strategy = "lib.id.UUIDv7Generator"
    )
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID userId;

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

    public void updateHubId(UUID hubId) {
        this.hubId = hubId;
    }
}
