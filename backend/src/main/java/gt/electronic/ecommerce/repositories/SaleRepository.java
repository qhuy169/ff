package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.Product;
import gt.electronic.ecommerce.entities.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author quang huy
 * @created 16/09/2025 - 9:02 PM
 * @project gt-backend
 */
@Repository
@Transactional
public interface SaleRepository extends JpaRepository<Sale, Long> {
        Optional<Sale> findByName(String name);

        @Query("select s "
                        + "from Sale s "
                        + "where "
                        + "(:title is null or (lower(s.name) like lower(concat('%', :title,'%')))) and "
                        + "(:fromPercent is null or s.percent >= :fromPercent) and "
                        + "(:toPercent is null or s.percent <= :toPercent) and "
                        + "(:fromDate is null or s.startDate >= :fromDate) and "
                        + "(:toDate is null or s.endDate <= :toDate)")
        Page<Sale> search(
                        @Param("title") String title,
                        @Param("fromPercent") Double fromPercent,
                        @Param("toPercent") Double toPercent,
                        @Param("fromDate") Date fromDate,
                        @Param("toDate") Date toDate,
                        Pageable pageable);

        @Query("select s from Sale s inner join s.products p where "
                        + "(p = :product) " +
                        "and s.startDate <= current_timestamp " +
                        "and s.endDate >= current_timestamp " +
                        "order by s.percent desc, s.endDate desc")
        List<Sale> findMostOptimalSaleByProduct(Product product);
}
