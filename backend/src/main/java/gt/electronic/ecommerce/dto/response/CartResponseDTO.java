package gt.electronic.ecommerce.dto.response;

import gt.electronic.ecommerce.dto.request.CartDetailCreationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * @author quang huy
 * @created 19/09/2025 - 4:59 PM
 * @project gt-backend
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {
  private UserSimpleResponseDTO user;
  private List<OrderDetailResponseDTO> cartItems;
}
