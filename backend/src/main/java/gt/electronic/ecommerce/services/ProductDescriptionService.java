package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.entities.ProductDescription;
import gt.electronic.ecommerce.entities.keys.ProductDescriptionKey;

/**
 * @author quang huy
 * @created 11/09/2025 - 8:48 PM
 */
public interface ProductDescriptionService {
  ProductDescription createOrUpdateProductDescription(ProductDescription productDescription);

  void deleteProductDescriptionById(ProductDescriptionKey id);
}
