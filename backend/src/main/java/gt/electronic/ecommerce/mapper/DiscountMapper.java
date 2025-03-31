package gt.electronic.ecommerce.mapper;

import gt.electronic.ecommerce.dto.response.DiscountResponseDTO;
import gt.electronic.ecommerce.entities.Discount;

/**
 * @author quang huy
 * @created 20/09/2025 - 9:54 PM
 * @project gt-backend
 */
public interface DiscountMapper {
  DiscountResponseDTO discountToDiscountResponseDTO(Discount entity);
}
