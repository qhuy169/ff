package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.dto.request.OrderCreationDTO;
import gt.electronic.ecommerce.dto.request.OrderUpdatePaymentDTO;
import gt.electronic.ecommerce.dto.request.OrderUpdateStatusDTO;
import gt.electronic.ecommerce.dto.request.UserCreationDTO;
import gt.electronic.ecommerce.dto.response.OrderResponseDTO;
import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:00 AM
 */
public interface OrderService {
  Page<OrderResponseDTO> getAllOrders(Pageable pageable);

  List<Map<String, Object>> getRecentOrders();

  List<Map<String, Object>> getLastSixMonthsRevenue();

  Long getOrderCount();

  Map<String, BigDecimal> getOrderStatistics();

  BigDecimal getTodayRevenue();

  Page<OrderResponseDTO> getAllOrdersByUser(String loginKey, Long userId, Pageable pageable);

  Page<OrderResponseDTO> getAllOrdersByShop(String loginKey, Long shopId, Pageable pageable, boolean... isAdmin);

  OrderResponseDTO getOrderById(String loginKey, Long id);

  OrderResponseDTO createOrder(String loginKey, OrderCreationDTO creationDTO);

  OrderResponseDTO updateOrder(String loginKey, Long id, OrderCreationDTO creationDTO);

  OrderResponseDTO updatePaymentOrder(String loginKey, Long id, OrderUpdatePaymentDTO updatePaymentDTO);

  OrderResponseDTO updateStatusOrder(String loginKey, Long id, OrderUpdateStatusDTO updateStatusDTO);

  OrderResponseDTO deleteOrderById(String loginKey, Long id);
}
