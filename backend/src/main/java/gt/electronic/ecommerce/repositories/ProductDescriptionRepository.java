package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.Discount;
import gt.electronic.ecommerce.entities.ProductDescription;
import gt.electronic.ecommerce.entities.keys.ProductDescriptionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author quang huy
 * @created 11/09/2025 - 8:22 PM
 * @project gt-backend
 */
@Repository
@Transactional
public interface ProductDescriptionRepository extends JpaRepository<ProductDescription, ProductDescriptionKey> {
}
