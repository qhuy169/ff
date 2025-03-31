package gt.electronic.ecommerce.mapper;

import gt.electronic.ecommerce.dto.request.CartDetailCreationDTO;
import gt.electronic.ecommerce.dto.request.OrderDetailCreationDTO;
import gt.electronic.ecommerce.dto.request.OrderShopCreationDTO;
import gt.electronic.ecommerce.dto.response.CartResponseDTO;
import gt.electronic.ecommerce.dto.response.OrderDetailResponseDTO;
import gt.electronic.ecommerce.dto.response.OrderResponseDTO;
import gt.electronic.ecommerce.entities.Order;
import gt.electronic.ecommerce.entities.OrderItem;
import gt.electronic.ecommerce.entities.OrderShop;
import gt.electronic.ecommerce.entities.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author quang huy
 * @created 20/09/2025 - 9:34 PM
 * @project gt-backend
 */
public interface OrderItemMapper {
  Set<OrderShop> orderDetailCreationDTOsToGroupOrderItemByShops(Order order,
      List<OrderShopCreationDTO> orderShopCreations);

  Set<OrderItem> cartDetailCreationDTOsToOrderItems(User user, List<CartDetailCreationDTO> creationDTOList);

  OrderDetailResponseDTO orderItemToOrderDetailResponseDTO(OrderItem entity);
}
