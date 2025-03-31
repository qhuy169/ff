package gt.electronic.ecommerce.mapper.impls;

import gt.electronic.ecommerce.dto.response.ShopPriceResponseDTO;
import gt.electronic.ecommerce.entities.ShopPrice;
import gt.electronic.ecommerce.mapper.ShopPriceMapper;
import org.springframework.stereotype.Component;

@Component
public class ShopPriceMapperImpl implements ShopPriceMapper {
    @Override
    public ShopPriceResponseDTO shopPriceToShopPriceResponseDTO(ShopPrice entity) {
        if (entity == null) {
            return null;
        }
        ShopPriceResponseDTO responseDTO = new ShopPriceResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setName(entity.getName());
        responseDTO.setTime(entity.getNumber() + " " + entity.getDateType().toString());
        responseDTO.setDescription(
                entity.getDescription() + "Bạn có thể đăng bán sản phẩm trên sàn giao dịch chúng tôi với tối đa " +
                        entity.getMaxProduct() + " sản phẩm trong thời gian " + responseDTO.getTime() + ".");
        responseDTO.setPrice(entity.getPrice());
        return responseDTO;
    }
}
