package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.Brand;
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
 * @project gt-backend
 */
@Repository
@Transactional
public interface BrandRepository extends JpaRepository<Brand, Integer> {
        @Query(value = "select b from Brand b where " +
                        "(:keyword is not null and lower(b.name) like lower(concat('%', :keyword,'%')))")
        List<Brand> findAll(String keyword);

        @Query(value = "select b from Brand b join b.categories c where " +
                        "(:keyword is not null and lower(b.name) like lower(concat('%', :keyword,'%'))) " +
                        "and (:category = c)")
        List<Brand> findAllByCategory(Category category, String keyword);

        @Query(value = "select distinct(p.brand) from Product p where p.shop = :shop")
        List<Brand> getALlBrandsByShop(@Param("shop") Shop shop);

        Optional<Brand> findByName(String name);

        Optional<Brand> findBySlug(String slug);
}
