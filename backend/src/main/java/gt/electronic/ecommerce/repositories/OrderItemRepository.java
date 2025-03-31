package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.OrderItem;
import gt.electronic.ecommerce.entities.Product;
import gt.electronic.ecommerce.entities.User;
import gt.electronic.ecommerce.models.enums.EOrdertemStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author quang huy
 * @created 14/10/2025 - 10:55 AM
 */
@Repository
@Transactional
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
  Optional<OrderItem> findByUserAndProductAndStatus(User user, Product product, EOrdertemStatus status);

  List<OrderItem> findByUserAndStatus(User user, EOrdertemStatus status, Pageable pageable);

  Optional<OrderItem> findByIdAndStatus(Long id, EOrdertemStatus status);
}
