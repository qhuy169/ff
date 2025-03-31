package gt.electronic.ecommerce.mapper;

import gt.electronic.ecommerce.dto.response.ShopPriceResponseDTO;
import gt.electronic.ecommerce.entities.ShopPrice;

public interface ShopPriceMapper {
    ShopPriceResponseDTO shopPriceToShopPriceResponseDTO(ShopPrice entity);
}
