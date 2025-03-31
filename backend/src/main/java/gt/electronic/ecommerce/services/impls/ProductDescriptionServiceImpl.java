package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.entities.ProductDescription;
import gt.electronic.ecommerce.entities.keys.ProductDescriptionKey;
import gt.electronic.ecommerce.repositories.ProductDescriptionRepository;
import gt.electronic.ecommerce.services.ProductDescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author quang huy
 * @created 11/09/2025 - 8:51 PM
 */
@Service
@Transactional
public class ProductDescriptionServiceImpl implements ProductDescriptionService {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  private ProductDescriptionRepository productDescriptionRepo;

  @Autowired
  public void ProductDescriptionRepository(ProductDescriptionRepository productDescriptionRepo) {
    this.productDescriptionRepo = productDescriptionRepo;
  }

  @Override
  public ProductDescription createOrUpdateProductDescription(ProductDescription productDescription) {
    ProductDescription productDescriptionFound = this.productDescriptionRepo
        .findById(productDescription.getId()).orElse(null);
    if (productDescriptionFound == null) {
      return this.productDescriptionRepo.save(productDescription);
    } else {
      productDescriptionFound.setValue(productDescription.getValue());
      return this.productDescriptionRepo.save(productDescriptionFound);
    }
  }

  @Override
  public void deleteProductDescriptionById(ProductDescriptionKey id) {
  }
}
