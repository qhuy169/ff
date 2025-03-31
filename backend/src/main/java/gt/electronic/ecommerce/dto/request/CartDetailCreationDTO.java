package gt.electronic.ecommerce.dto.request;

import lombok.Data;
import lombok.Getter;

/**
 * @author quang huy
 * @created 19/09/2025 - 5:00 PM
 * @project gt-backend
 */
@Data
public class CartDetailCreationDTO {
  private Long productId;
  private Long quantity;
}
