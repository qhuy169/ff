package gt.electronic.ecommerce.mapper;

import gt.electronic.ecommerce.dto.response.SaleResponseDTO;
import gt.electronic.ecommerce.entities.Sale;

/**
 * @author quang huy
 * @created 16/09/2025 - 8:47 PM
 * @project gt-backend
 */
public interface SaleMapper {
  SaleResponseDTO saleToSaleResponseDTO(Sale entity, boolean... isFull);
}
