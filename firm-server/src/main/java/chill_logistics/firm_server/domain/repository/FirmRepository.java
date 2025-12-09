package chill_logistics.firm_server.domain.repository;

import chill_logistics.firm_server.domain.entity.Firm;
import java.math.BigDecimal;

public interface FirmRepository {

    void save(Firm firm);

    boolean existsByNameAndDeletedAtIsNull(String name);
    boolean existsByFullAddressAndDeletedAtIsNull(String fullAddress);
    boolean existsByLatitudeAndLongitudeAndDeletedAtIsNull(BigDecimal latitude, BigDecimal longitude);



}
