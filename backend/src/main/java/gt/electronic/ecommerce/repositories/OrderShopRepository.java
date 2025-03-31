package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.Order;
import gt.electronic.ecommerce.entities.OrderShop;
import gt.electronic.ecommerce.entities.Shop;
import gt.electronic.ecommerce.entities.User;
import gt.electronic.ecommerce.entities.keys.OrderShopKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author quang huy
 * @created 03/12/2025 - 8:26 PM
 */
public interface OrderShopRepository extends JpaRepository<OrderShop, OrderShopKey> {

        @Query("select os from OrderShop os where os.order.user = :user")
        Page<OrderShop> findAllByUser(@Param("user") User user, Pageable pageable);

        Page<OrderShop> findAllByShop(Shop shop, Pageable pageable);

        Optional<OrderShop> findById(Long id);

        @Query("select o from OrderShop o where " +
                        "o.shop = :shop and" +
                        "(:startDate is not null and o.createdAt >= :startDate) " +
                        "and (:endDate is not null and o.createdAt <= :endDate)" +
                        "order by o.createdAt desc")
        List<OrderShop> findAllByShopAndRangePayDate(
                        @Param("shop") Shop shop,
                        @Param("startDate") Date startDate,
                        @Param("endDate") Date endDate);
}
