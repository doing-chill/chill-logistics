package chill_logistics.hub_server.domain.entity;

import jakarta.persistence.*;
import lib.entity.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@Table(name = "p_hub")
public class Hub extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuidv7")
    @GenericGenerator(
            name = "uuidv7",
            strategy = "lib.id.UUIDv7Generator"
    )
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "hub_manager_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID hubManagerId;

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

    @Column(name = "building_name", length = 50)
    private String buildingName;

    @Column(name = "detail_address", length = 100)
    private String detailAddress;

    @Column(name = "full_address", nullable = false, length = 100)
    private String fullAddress;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    public static Hub create(
        String name,
        UUID hubManagerId,
        String postalCode,
        String country,
        String region,
        String city,
        String district,
        String roadName,
        String buildingName,
        String detailAddress,
        String fullAddress,
        BigDecimal latitude,
        BigDecimal longitude
    ) {
        Hub hub = new Hub();
        hub.name = name;
        hub.hubManagerId = hubManagerId;
        hub.postalCode = postalCode;
        hub.country = country;
        hub.region = region;
        hub.city = city;
        hub.district = district;
        hub.roadName = roadName;
        hub.buildingName = buildingName;
        hub.detailAddress = detailAddress;
        hub.fullAddress = fullAddress;
        hub.latitude = latitude;
        hub.longitude = longitude;
        return hub;
    }

    public void update(
        String name,
        UUID hubManagerId,
        String postalCode,
        String country,
        String region,
        String city,
        String district,
        String roadName,
        String buildingName,
        String detailAddress,
        String fullAddress,
        BigDecimal latitude,
        BigDecimal longitude){

        if (name != null) this.name = name;
        if (hubManagerId != null) this.hubManagerId = hubManagerId;
        if (postalCode != null) this.postalCode = postalCode;
        if (country != null) this.country = country;
        if (region != null) this.region = region;
        if (city != null) this.city = city;
        if (district != null) this.district = district;
        if (roadName != null) this.roadName = roadName;
        if (buildingName != null) this.buildingName = buildingName;
        if (detailAddress != null) this.detailAddress = detailAddress;
        if (fullAddress != null) this.fullAddress = fullAddress;
        if (latitude != null) this.latitude = latitude;
        if (longitude != null) this.longitude = longitude;
    }




}