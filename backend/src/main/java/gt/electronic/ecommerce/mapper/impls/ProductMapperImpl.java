package gt.electronic.ecommerce.mapper.impls;

import gt.electronic.ecommerce.dto.response.ProductGalleryDTO;
import gt.electronic.ecommerce.dto.response.ProductResponseDTO;
import gt.electronic.ecommerce.entities.Image;
import gt.electronic.ecommerce.entities.Product;
import gt.electronic.ecommerce.entities.ProductDescription;
import gt.electronic.ecommerce.entities.Sale;
import gt.electronic.ecommerce.mapper.ProductMapper;
import gt.electronic.ecommerce.mapper.ShopMapper;
import gt.electronic.ecommerce.models.clazzs.ProductRating;
import gt.electronic.ecommerce.models.clazzs.ProductSentiment;
import gt.electronic.ecommerce.models.enums.ESentiment;
import gt.electronic.ecommerce.services.FeedbackService;
import gt.electronic.ecommerce.services.ProductService;
import gt.electronic.ecommerce.services.SaleService;
import gt.electronic.ecommerce.services.ShopService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static gt.electronic.ecommerce.utils.Utils.IMAGE_DEFAULT_PATH;
import static gt.electronic.ecommerce.utils.Utils.toBeTruncatedDouble;

/**
 * @author quang huy
 * @created 11/09/2025 - 9:18 PM
 * @project gt-backend
 */
@Component
public class ProductMapperImpl implements ProductMapper {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  private FeedbackService feedbackService;

  @Autowired
  public void FeedbackService(FeedbackService feedbackService) {
    this.feedbackService = feedbackService;
  }

  private ProductService productService;

  @Autowired
  public void ProductService(ProductService productService) {
    this.productService = productService;
  }

  private SaleService saleService;

  @Autowired
  public void SaleService(SaleService saleService) {
    this.saleService = saleService;
  }

  private ShopMapper shopMapper;

  @Autowired
  public void ShopMapper(ShopMapper shopMapper) {
    this.shopMapper = shopMapper;
  }

  @Override
  public ProductResponseDTO productToProductResponseDTO(Product entity, Boolean... haveSentiment) {
    if (entity == null) {
      return null;
    }
    ProductResponseDTO responseDTO = new ProductResponseDTO();
    responseDTO.setId(entity.getId());
    if (entity.getThumbnail() != null) {
      responseDTO.setImg(Utils.getUrlFromPathImage(entity.getThumbnail().getPath()));
    } else {
      responseDTO.setImg(Utils.getUrlFromPathImage(IMAGE_DEFAULT_PATH));
    }
    responseDTO.setTitle(entity.getName());
    responseDTO.setAvailableQuantity(entity.getQuantity());
    responseDTO.setSoldQuantity(this.productService.getSoldQuantityById(entity.getId()));
    if (entity.getShop() != null) {
      responseDTO.setShop(this.shopMapper.shopToShopSimpleResponseDTO(entity.getShop()));
    }
    // responseDTO.setUrl();
    responseDTO.setSlug(entity.getSlug());
    responseDTO.setOriginPrice(entity.getPrice());
    // responseDTO.getPromotion()
    Sale sale = this.saleService.getMostOptimalSaleByProduct(entity.getId());
    if (sale != null) {
      BigDecimal salePrice = Utils.getPriceProduct(entity, sale);
      responseDTO.setPrice(salePrice);
      responseDTO.setDiscount(sale.getPercent());
      responseDTO.setTag(sale.getName());
    } else {
      responseDTO.setPrice(entity.getPrice());
    }
    ProductRating productRating = this.feedbackService.getProductRatingByProduct(entity.getId());
    if (productRating != null) {
      responseDTO.setStar(productRating.getStar());
      responseDTO.setTotalVote(productRating.getTotalVote());
      responseDTO.setVote(productRating.getVoteDetails());
    } else {
      responseDTO.setStar(0);
      responseDTO.setTotalVote(0);
    }
    if (haveSentiment.length > 0 && haveSentiment[0]) {
      ProductSentiment productSentiment = this.feedbackService.getProductSentimentByProduct(entity.getId());
      if (productSentiment == null) {
        productSentiment.setSentiment(ESentiment.SENTIMENT_UNKNOWN.toString());
        productSentiment.setTotalSentiment(0);
      }
      responseDTO.setSentiment(productSentiment);
    }
    if (entity.getBrand() != null) {
      responseDTO.setBrand(entity.getBrand().getName());
      responseDTO.setBrandSlug(entity.getBrand().getSlug());
    }
    if (entity.getCategory() != null) {
      responseDTO.setCategory(entity.getCategory().getName());
      responseDTO.setCategorySlug(entity.getCategory().getSlug());
    }
    // responseDTO.setLocation();
    if (entity.getImageGallery() != null && entity.getImageGallery().size() > 0) {
      String[] gallery = new String[entity.getImageGallery().size()];
      int i = 0;
      for (Image image : entity.getImageGallery()) {
        gallery[i] = Utils.getUrlFromPathImage(image.getPath());
        i++;
      }
      responseDTO.setGallery(gallery);
    }
    if (entity.getLocation() != null) {
      responseDTO.setLocation(Utils.getLocationStringFromLocation(entity.getLocation()));
    }
    if (entity.getDescriptions() != null && entity.getDescriptions().size() > 0) {
      // DescriptionResponseDTO[] parameter = new
      // DescriptionResponseDTO[entity.getDescriptions().size()];
      Map<String, String> parameter = new HashMap<>();
      int i = 0;
      for (ProductDescription productDescription : entity.getDescriptions()) {
        parameter.put(productDescription.getDescription().getName(), productDescription.getValue());
        // parameter[i] = new DescriptionResponseDTO();
        // parameter[i].setName(productDescription.getDescription().getName());
        // parameter[i].setValue(productDescription.getValue());
        // parameter[i].setType();
        i++;
      }
      responseDTO.setParameter(parameter);
    }
    responseDTO.setInfo(entity.getDescription());
    responseDTO.setStatus(entity.getStatus().ordinal());
    responseDTO.setEnabled(entity.isEnabled());
    return responseDTO;
  }

  @Override
  public ProductGalleryDTO productToProductGalleryDTO(Product entity) {
    if (entity == null) {
      return null;
    }
    ProductGalleryDTO responseDTO = new ProductGalleryDTO();
    responseDTO.setId(entity.getId());
    if (entity.getThumbnail() != null) {
      responseDTO.setImg(Utils.getUrlFromPathImage(entity.getThumbnail().getPath()));
    } else {
      responseDTO.setImg(Utils.getUrlFromPathImage(IMAGE_DEFAULT_PATH));
    }
    responseDTO.setTitle(entity.getName());
    responseDTO.setAvailableQuantity(entity.getQuantity());
    responseDTO.setSoldQuantity(this.productService.getSoldQuantityById(entity.getId()));
    // responseDTO.setUrl();
    responseDTO.setSlug(entity.getSlug());
    responseDTO.setOriginPrice(entity.getPrice());
    // responseDTO.getPromotion()
    // responseDTO.setLocation();
    Sale sale = this.saleService.getMostOptimalSaleByProduct(entity.getId());
    if (sale != null) {
      responseDTO.setPrice(Utils.getPriceProduct(entity, sale));
      responseDTO.setDiscount(sale.getPercent());
      responseDTO.setTag(sale.getName());
    } else {
      responseDTO.setPrice(entity.getPrice());
    }
    ProductRating productRating = this.feedbackService.getProductRatingByProduct(entity.getId());
    if (productRating != null) {
      responseDTO.setStar(toBeTruncatedDouble(productRating.getStar()));
      responseDTO.setTotalVote(productRating.getTotalVote());
    } else {
      responseDTO.setStar(0);
      responseDTO.setTotalVote(0);
    }
    if (entity.getBrand() != null) {
      responseDTO.setBrand(entity.getBrand().getName());
      responseDTO.setBrandSlug(entity.getBrand().getSlug());
    }
    if (entity.getCategory() != null) {
      responseDTO.setCategory(entity.getCategory().getName());
      responseDTO.setCategorySlug(entity.getCategory().getSlug());
    }
    responseDTO.setStatus(entity.getStatus().ordinal());
    responseDTO.setEnabled(entity.isEnabled());
    return responseDTO;
  }
}
