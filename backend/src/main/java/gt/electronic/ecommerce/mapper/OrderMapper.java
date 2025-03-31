package gt.electronic.ecommerce.mapper;

import gt.electronic.ecommerce.dto.response.BrandResponseDTO;
import gt.electronic.ecommerce.dto.response.OrderResponseDTO;
import gt.electronic.ecommerce.entities.Brand;
import gt.electronic.ecommerce.entities.Order;
import gt.electronic.ecommerce.entities.OrderShop;
import gt.electronic.ecommerce.models.clazzs.OrderPaymentOnly;

/**
 * @author quang huy
 * @created 20/09/2025 - 9:34 PM
 * @project gt-backend
 */
public interface OrderMapper {
  OrderResponseDTO orderToOrderResponseDTO(Order entity, Long shopId, boolean... isFull);

  OrderResponseDTO orderShopToOrderResponseDTO(OrderShop entity, Long shopId, boolean... isFull);

  OrderPaymentOnly orderToOrderPaymentOnly(Order entity);
}
