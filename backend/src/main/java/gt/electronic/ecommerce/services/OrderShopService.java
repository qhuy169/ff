package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.dto.request.OrderUpdateStatusDTO;
import gt.electronic.ecommerce.dto.response.OrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author quang huy
 * @created 23/12/2025 - 7:40 PM
 */
public interface OrderShopService {
  Page<OrderResponseDTO> getAllOrderShops(Pageable pageable);

  Page<OrderResponseDTO> getAllOrderShopsByUser(String loginKey, Long userId, Pageable pageable);

  Page<OrderResponseDTO> getAllOrderShopsByShop(String loginKey, Long shopId, Pageable pageable, boolean... isAdmin);

  OrderResponseDTO getOrderShopByOrderId(String loginKey, Long orderId);

  OrderResponseDTO updateStatusOrderShop(String loginKey, Long id, OrderUpdateStatusDTO updateStatusDTO);
}
