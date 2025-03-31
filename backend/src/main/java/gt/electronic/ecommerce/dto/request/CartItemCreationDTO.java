package gt.electronic.ecommerce.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author quang huy
 * @created 19/09/2025 - 4:51 PM
 * @project gt-backend
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemCreationDTO {
  private Long userId;
  private Long productId;
  private Integer quantity;
}
