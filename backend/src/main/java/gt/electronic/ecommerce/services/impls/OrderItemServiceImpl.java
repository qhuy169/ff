package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.entities.OrderItem;
import gt.electronic.ecommerce.repositories.OrderItemRepository;
import gt.electronic.ecommerce.repositories.ProductRepository;
import gt.electronic.ecommerce.services.OrderItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author quang huy
 * @created 14/10/2025 - 10:54 AM
 */
@Service
@Transactional
public class OrderItemServiceImpl implements OrderItemService {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = OrderItem.class.getSimpleName();
  private OrderItemRepository orderItemRepo;

  @Autowired
  public void OrderItemRepository(OrderItemRepository orderItemRepo) {
    this.orderItemRepo = orderItemRepo;
  }

  private ProductRepository productRepo;

  @Autowired
  public void ProductRepository(ProductRepository productRepo) {
    this.productRepo = productRepo;
  }

  @Override
  public void deleteOrderItem(OrderItem orderItem) {
    orderItem.getProduct().setQuantity(orderItem.getQuantity() + orderItem.getProduct().getQuantity());
    this.productRepo.save(orderItem.getProduct());
    this.orderItemRepo.deleteById(orderItem.getId());
  }
}
