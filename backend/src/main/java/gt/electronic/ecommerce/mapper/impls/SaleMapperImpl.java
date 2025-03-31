package gt.electronic.ecommerce.mapper.impls;

import gt.electronic.ecommerce.dto.response.ProductGalleryDTO;
import gt.electronic.ecommerce.dto.response.SaleResponseDTO;
import gt.electronic.ecommerce.entities.Product;
import gt.electronic.ecommerce.entities.Sale;
import gt.electronic.ecommerce.mapper.ProductMapper;
import gt.electronic.ecommerce.mapper.SaleMapper;
import gt.electronic.ecommerce.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author quang huy
 * @created 16/09/2025 - 8:47 PM
 * @project gt-backend
 */
@Component
public class SaleMapperImpl implements SaleMapper {
  private ProductMapper productMapper;

  @Autowired
  public void ProductMapper(ProductMapper productMapper) {
    this.productMapper = productMapper;
  }

  private UserMapper userMapper;

  @Autowired
  public void UserMapper(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public SaleResponseDTO saleToSaleResponseDTO(Sale entity, boolean... isFull) {
    if (entity == null) {
      return null;
    }
    SaleResponseDTO responseDTO = new SaleResponseDTO();
    responseDTO.setId(entity.getId());
    responseDTO.setName(entity.getName());
    responseDTO.setDescription(entity.getDescription());
    responseDTO.setPercent(entity.getPercent());
    if (isFull.length > 0 && isFull[0] && entity.getProducts() != null) {
      ProductGalleryDTO[] productGalleries = new ProductGalleryDTO[entity.getProducts().size()];
      int i = 0;
      for (Product product : entity.getProducts()) {
        productGalleries[i] = new ProductGalleryDTO();
        productGalleries[i] = this.productMapper.productToProductGalleryDTO(product);
        i++;
      }
      responseDTO.setProductGalleries(productGalleries);
    }
    if (entity.getCreator() != null) {
      responseDTO.setCreator(this.userMapper.userToUserSimpleResponseDTO(entity.getCreator()));
    }
    responseDTO.setStartDate(entity.getStartDate());
    responseDTO.setEndDate(entity.getEndDate());
    if (entity.getThumbnail() != null) {
      responseDTO.setThumbnail(entity.getThumbnail().getPath());
    }
    return responseDTO;
  }
}
