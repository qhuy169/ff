package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.dto.request.CartDetailCreationDTO;
import gt.electronic.ecommerce.dto.request.CartItemCreationDTO;
import gt.electronic.ecommerce.dto.response.OrderDetailResponseDTO;
import gt.electronic.ecommerce.dto.response.ResponseObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:00 AM
 */
public interface CartItemService {
  List<OrderDetailResponseDTO> getAllCartItemByUser(String loginKey, Long userId, Pageable pageable);

  OrderDetailResponseDTO getCartItemById(Long id);

  OrderDetailResponseDTO getCartItemByUserAndProduct(Long userId, Long productId);

  OrderDetailResponseDTO createCartItem(String loginKey, CartItemCreationDTO creationDTO);

  OrderDetailResponseDTO updateCartItem(String loginKey, Long id, CartItemCreationDTO creationDTO);

  OrderDetailResponseDTO deleteCartItemById(String loginKey, Long userId, Long id);

  List<OrderDetailResponseDTO> updateCart(String loginKey, List<CartDetailCreationDTO> creationDTOList);
}
