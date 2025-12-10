package chill_logistics.firm_server.domain.repository;

import chill_logistics.firm_server.domain.entity.Firm;
import chill_logistics.firm_server.domain.entity.FirmType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FirmRepository {

    void save(Firm firm);

    boolean existsByNameAndDeletedAtIsNull(String name);
    boolean existsByFullAddressAndDeletedAtIsNull(String fullAddress);
    boolean existsByLatitudeAndLongitudeAndDeletedAtIsNull(BigDecimal latitude, BigDecimal longitude);

    Optional<Firm> findById(UUID firmId);

    Optional<Firm> findByIdAndFirmTypeAndDeletedAtIsNull(UUID firmId, FirmType firmType);

    List<Firm> findAll(int page, int size);
}
