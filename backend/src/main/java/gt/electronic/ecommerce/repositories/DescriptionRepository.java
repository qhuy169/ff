package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.Category;
import gt.electronic.ecommerce.entities.Description;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author quang huy
 * @created 11/09/2025 - 8:21 PM
 * @project gt-backend
 */
public interface DescriptionRepository extends JpaRepository<Description, Long> {

  Optional<Description> findByName(String name);

  Optional<Description> findByNameAndCategories(String name, Category category);
}
