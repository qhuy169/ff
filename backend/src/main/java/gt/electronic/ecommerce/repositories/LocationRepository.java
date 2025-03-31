package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author quang huy
 * @created 26/09/2025 - 3:28 PM
 * @project gt-backend
 */
public interface LocationRepository extends JpaRepository<Location, Long> {

        @Query("select l from Location l where " +
                        "(:commune is null or (l.commune is not null and l.commune = :commune)) " +
                        "and (:district is null or (l.district is not null and l.district = :district)) " +
                        "and (:province is not null and l.province = :province)")
        List<Location> findAllByCommuneAndDistrictAndProvince(
                        @Param("commune") String commune,
                        @Param("district") String district,
                        @Param("province") String province);
}
