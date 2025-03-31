package gt.electronic.ecommerce.mapper.impls;

import gt.electronic.ecommerce.dto.response.DiscountResponseDTO;
import gt.electronic.ecommerce.entities.Discount;
import gt.electronic.ecommerce.mapper.DiscountMapper;
import gt.electronic.ecommerce.repositories.DiscountRepository;
import gt.electronic.ecommerce.utils.Utils;
import org.springframework.stereotype.Component;

/**
 * @author quang huy
 * @created 20/09/2025 - 9:55 PM
 * @project gt-backend
 */
@Component
public class DiscountMapperImpl implements DiscountMapper {
  @Override
  public DiscountResponseDTO discountToDiscountResponseDTO(Discount entity) {
    if (entity == null) {
      return null;
    }
    DiscountResponseDTO responseDTO = new DiscountResponseDTO();
    responseDTO.setId(entity.getId());
    responseDTO.setName(entity.getName());
    responseDTO.setDescription(entity.getDescription());
    responseDTO.setQuantity(entity.getQuantity());
    responseDTO.setPercent(entity.getPercent());
    responseDTO.setCode(entity.getCode());
    responseDTO.setCappedAt(entity.getCappedAt());
    responseDTO.setPrice(entity.getPrice());
    responseDTO.setMinSpend(entity.getMinSpend());
    if (entity.getShop() != null) {
      responseDTO.setShopId(entity.getShop().getId());
    }
    responseDTO.setStartDate(entity.getStartDate());
    responseDTO.setEndDate(entity.getEndDate());
    responseDTO.setType(entity.getType().ordinal());
    if (entity.getThumbnail() != null) {
      responseDTO.setThumbnail(Utils.getUrlFromPathImage(entity.getThumbnail().getPath()));
    }
    return responseDTO;
  }
}
