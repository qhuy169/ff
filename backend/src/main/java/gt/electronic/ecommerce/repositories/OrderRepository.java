package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.Order;
import gt.electronic.ecommerce.entities.OrderShop;
import gt.electronic.ecommerce.entities.Shop;
import gt.electronic.ecommerce.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:03 AM
 * @project gt-backend
 */
@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
        Page<Order> findAllByUser(User user, Pageable pageable);

        @Query("SELECT p.name FROM OrderItem oi " +
                        "JOIN oi.product p " +
                        "JOIN oi.orderShop os " +
                        "WHERE os.order.id = :orderId")
        List<String> findProductNamesByOrderId(@Param("orderId") Long orderId);

        List<Order> findTop10ByOrderByCreatedAtDesc();

        @Query(value = "select distinct o from Order o inner join OrderShop os on o = os.order where os.shop = :shop")
        Page<Order> findAllByShop(Shop shop, Pageable pageable);

        @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.createdAt BETWEEN :startOfDay AND :endOfDay AND o.status = 'ORDER_COMPLETED'")
        BigDecimal calculateRevenueBetween(@Param("startOfDay") Date startOfDay,
                        @Param("endOfDay") Date endOfDay);

        List<Order> findAllByPaymentOrderCode(String paymentOrderCode);

        @Query(value = "select os from Order o inner join OrderShop os on o = os.order" +
                        " where os.shipOrderCode is null" +
                        // " where os.status =
                        // gt.electronic.ecommerce.models.enums.EOrderStatus.ORDER_PENDING" +
                        " and o.location.province = :province " +
                        // " and :province is not null" +
                        " and :commune is not null and :district is not null")
        Page<OrderShop> getAllOrderByShipperArea(@Param("commune") String commune,
                        @Param("district") String district,
                        @Param("province") String province, Pageable pageable);
}
