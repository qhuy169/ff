package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.Discount;
import gt.electronic.ecommerce.entities.Shop;
import gt.electronic.ecommerce.entities.User;
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
 * @created 11/09/2025 - 10:03 AM
 * @project gt-backend
 */
@Repository
@Transactional
public interface DiscountRepository extends JpaRepository<Discount, Long> {
        Optional<Discount> findByCode(String code);

        Page<Discount> findAllByShop(Shop shop, Pageable pageable);

        @Query("select d from Discount d join d.users user " +
                        "where user = :user")
        List<Discount> findAllByUser(@Param("user") User user, Pageable pageable);

        @Query("SELECT d "
                        + "FROM Discount d "
                        + "WHERE "
                        + "d.code = :code AND "
                        + "(d.startDate <= current_timestamp) AND "
                        + "(d.endDate IS NULL OR d.endDate >= current_timestamp)")
        List<Discount> checkDiscountByCode(@Param("code") String code);

        @Query("SELECT d "
                        + "FROM Discount d "
                        + "WHERE "
                        + "(:title IS NULL or (LOWER(d.name) LIKE LOWER(concat('%', :title,'%')))) AND "
                        + "(:percent IS NULL OR d.percent = :percent) AND "
                        + "(:code IS NULL OR (LOWER(d.code) LIKE LOWER(concat('%', :code, '%')))) AND "
                        + "(:startDate IS NULL OR :startDate = d.startDate) AND "
                        + "(:endDate IS NULL OR d.endDate = :endDate) AND "
                        + "(:fromPercent IS NULL OR d.percent >= :fromPercent) AND "
                        + "(:toPercent IS NULL OR d.percent <= :toPercent) AND"
                        + "(:fromDate IS NULL OR d.startDate >= :fromDate) AND "
                        + "(:toDate IS NULL OR d.endDate <= :toDate ) AND "
                        + "(:shop IS NULL OR d.shop = :shop)")
        Page<Discount> search(
                        @Param("title") String title,
                        @Param("percent") Double percent,
                        @Param("code") String code,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate,
                        @Param("fromPercent") Double fromPercent,
                        @Param("toPercent") Double toPercent,
                        @Param("fromDate") Date fromDate,
                        @Param("toDate") Date toDate,
                        @Param("shop") Shop shop, Pageable pageable);
}
