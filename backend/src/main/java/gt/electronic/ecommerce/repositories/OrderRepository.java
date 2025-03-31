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

        @Query(value = "select distinct o from Order o inner join OrderShop os on o = os.order where os.shop = :shop")
        Page<Order> findAllByShop(Shop shop, Pageable pageable);

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
