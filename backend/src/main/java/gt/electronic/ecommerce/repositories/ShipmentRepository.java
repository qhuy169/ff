package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.Shipment;
import gt.electronic.ecommerce.entities.User;
import gt.electronic.ecommerce.models.enums.EShipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ShipmentRepository extends JpaRepository<Shipment, String> {
    Page<Shipment> findAllByUser(User shipper, Pageable pageable);

    @Query(value = "select s from Shipment s" +
            " where s.user = :shipper" +
            " and (:status is null or s.status = :status)")
    Page<Shipment> findAllByUserAndAndStatus(@Param("shipper") User shipper,
                                             @Param("status") EShipmentStatus status,
                                             Pageable pageable);
}
