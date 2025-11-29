package chill_logistics.firm_server.domain.entity;

import jakarta.persistence.*;
import lib.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Entity
@Table(name = "p_firm")
public class Firm extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuidv7")
    @GenericGenerator(
            name = "uuidv7",
            strategy = "lib.id.UUIDv7Generator"
    )
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "hub_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID hubId;

    @Column(name = "owner_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID ownerId;

    @Column(name = "owner_name", nullable = false, length = 15)
    private String ownerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "firm_type", nullable = false, length = 15)
    private FirmType firmType;

    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Column(name = "region", nullable = false, length = 50)
    private String region;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "district", nullable = false, length = 50)
    private String district;

    @Column(name = "road_name", nullable = false, length = 100)
    private String roadName;

    @Column(name = "building_name", length = 100)
    private String buildingName;

    @Column(name = "detail_address", length = 100)
    private String detailAddress;

    @Column(name = "full_address", nullable = false, columnDefinition = "TEXT")
    private String fullAddress;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private double latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private double longitude;
}
