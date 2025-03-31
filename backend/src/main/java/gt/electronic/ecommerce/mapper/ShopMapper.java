package gt.electronic.ecommerce.mapper;

import gt.electronic.ecommerce.dto.response.ShopResponseDTO;
import gt.electronic.ecommerce.dto.response.ShopSimpleResponseDTO;
import gt.electronic.ecommerce.entities.Shop;

/**
 * @author quang huy
 * @created 01/11/2025 - 8:33 PM
 */
public interface ShopMapper {
  ShopResponseDTO shopToShopResponseDTO(Shop entity);

  ShopSimpleResponseDTO shopToShopSimpleResponseDTO(Shop entity);
}
