package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.Category;
import gt.electronic.ecommerce.entities.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:02 AM
 */
@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category, Integer> {
  @Query(value = "select c from Category c where " +
      "lower(c.name) like lower(concat('%', :keyword,'%'))")
  List<Category> findAll(String keyword);

  @Query(value = "select distinct(p.category) from Product p where p.shop = :shop")
  List<Category> getALlCategoriesByShop(@Param("shop") Shop shop);

  Optional<Category> findByName(String name);

  Optional<Category> findBySlug(String slug);
}
