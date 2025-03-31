package gt.electronic.ecommerce.mapper.impls;

import gt.electronic.ecommerce.dto.response.BrandResponseDTO;
import gt.electronic.ecommerce.entities.Brand;
import gt.electronic.ecommerce.mapper.BrandMapper;
import gt.electronic.ecommerce.utils.Utils;
import org.springframework.stereotype.Component;

import static gt.electronic.ecommerce.utils.Utils.IMAGE_DEFAULT_PATH;

/**
 * @author quang huy
 * @created 11/09/2025 - 12:38 PM
 * @project gt-backend
 */
@Component
public class BrandMapperImpl implements BrandMapper {
  @Override
  public BrandResponseDTO brandToBrandResponseDTO(Brand entity) {
    if (entity == null) {
      return null;
    }
    BrandResponseDTO responseDTO = new BrandResponseDTO();
    responseDTO.setId(entity.getId());
    responseDTO.setName(entity.getName());
    responseDTO.setDescription(entity.getDescription());
    if (entity.getImage() != null) {
      responseDTO.setImg(Utils.getUrlFromPathImage(entity.getImage().getPath()));
    }
    // } else {
    // responseDTO.setImg(Utils.getUrlFromPathImage(IMAGE_DEFAULT_PATH));
    // }
    return responseDTO;
  }
}
