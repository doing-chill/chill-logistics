package chill_logistics.firm_server.infrastructure.repository;

import chill_logistics.firm_server.domain.entity.Firm;
import chill_logistics.firm_server.domain.entity.FirmType;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaFirmRepository extends JpaRepository<Firm, UUID> {

    boolean existsByNameAndDeletedAtIsNull(String name);
    boolean existsByFullAddressAndDeletedAtIsNull(String fullAddress);
    boolean existsByLatitudeAndLongitudeAndDeletedAtIsNull(BigDecimal latitude, BigDecimal longitude);

    Optional<Firm> findByIdAndDeletedAtIsNull(UUID firmId);

    Optional<Firm> findByIdAndFirmTypeAndDeletedAtIsNull(UUID firmId, FirmType firmType);
}
