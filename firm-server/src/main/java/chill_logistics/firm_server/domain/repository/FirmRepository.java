package chill_logistics.firm_server.domain.repository;

import chill_logistics.firm_server.domain.entity.Firm;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface FirmRepository {

    void save(Firm firm);

    boolean existsByNameAndDeletedAtIsNull(String name);
    boolean existsByFullAddressAndDeletedAtIsNull(String fullAddress);
    boolean existsByLatitudeAndLongitudeAndDeletedAtIsNull(BigDecimal latitude, BigDecimal longitude);

    Optional<Firm> findById(UUID firmId);



}
