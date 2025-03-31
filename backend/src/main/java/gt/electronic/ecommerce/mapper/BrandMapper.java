package gt.electronic.ecommerce.mapper;

import gt.electronic.ecommerce.dto.response.BrandResponseDTO;
import gt.electronic.ecommerce.entities.Brand;

/**
 * @author quang huy
 * @created 11/09/2025 - 12:37 PM
 * @project gt-backend
 */
public interface BrandMapper {
  BrandResponseDTO brandToBrandResponseDTO(Brand entity);
}
