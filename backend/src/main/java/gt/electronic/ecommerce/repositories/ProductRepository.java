package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.*;
import gt.electronic.ecommerce.models.enums.EProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:02 AM
 * @project gt-backend
 */
@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {

        Optional<Product> findByName(String name);

        Optional<Product> findBySlug(String slug);

        @Query(value = "select p from Product p "
                        + "where "
                        + "(:keyword is null "
                        + "or length(:keyword) < 1 "
                        + "or (lower(p.name) like lower(concat('%', :keyword,'%'))) "
                        + "or (p.brand is not null and lower(p.brand.name) like lower(concat('%', :keyword,'%'))) "
                        + "or (p.category is not null and lower(p.category.name) like lower(concat('%', :keyword,'%'))) "
                        + "or (p.shop is not null and lower(p.shop.name) like lower(concat('%', :keyword,'%')))) "
                        + "and (coalesce(:brands, null) is null or p.brand in (:brands)) "
                        + "and (coalesce(:categories, null) is null or (p.category in (:categories) "
                        + "or p.category.parentCategory in (:categories))) "
                        + "and (:shop is null or (p.shop is not null and p.shop = :shop)) "
                        + "and (:location is null or p.location is null or p.location = :location) "
                        + "and p.price > :minPrice "
                        + "and p.price < :maxPrice " +
                        "and (:shop is null or p.enabled = true)")
        List<Product> filterProductToList(
                        @Param("keyword") String keyword,
                        @Param("categories") List<Category> categories,
                        @Param("brands") List<Brand> brands,
                        @Param("shop") Shop shop,
                        @Param("location") Location location,
                        @Param("minPrice") BigDecimal minPrice,
                        @Param("maxPrice") BigDecimal maxPrice);

        @Query(value = "select p from Product p "
                        + "where "
                        + "(:keyword is null "
                        + "or length(:keyword) < 1 "
                        + "or (lower(p.name) like lower(concat('%', :keyword,'%'))) "
                        + "or (p.brand is not null and lower(p.brand.name) like lower(concat('%', :keyword,'%'))) "
                        + "or (p.category is not null and lower(p.category.name) like lower(concat('%', :keyword,'%'))) "
                        + "or (p.shop is not null and lower(p.shop.name) like lower(concat('%', :keyword,'%')))) "
                        + "and (coalesce(:brands, null) is null or p.brand in (:brands)) "
                        + "and (coalesce(:categories, null)is null or (p.category in (:categories) "
                        + "or p.category.parentCategory in (:categories))) "
                        + "and (:shop is null or (p.shop is not null and p.shop = :shop)) "
                        + "and (:location is null or p.location is null or p.location = :location) "
                        + "and p.price > :minPrice "
                        + "and p.price < :maxPrice " +
                        "and (:shop is null or p.enabled = true)")
        Page<Product> filterProductToPage(
                        @Param("keyword") String keyword,
                        @Param("categories") List<Category> categories,
                        @Param("brands") List<Brand> brands,
                        @Param("shop") Shop shop,
                        @Param("location") Location location,
                        @Param("minPrice") BigDecimal minPrice,
                        @Param("maxPrice") BigDecimal maxPrice,
                        Pageable pageable);

        Page<Product> findAllByStatus(EProductStatus status, Pageable pageable);

        @Query(value = "select p from Product p" +
                        " where " +
                        " p.brand = :brand" +
                        " and p.enabled = true")
        Page<Product> findAllByBrand(@Param("brand") Brand brand, Pageable pageable);

        @Query(value = "select p from Product p" +
                        " where " +
                        " p.category = :category" +
                        " and p.enabled = true")
        Page<Product> findAllByCategory(@Param("category") Category category, Pageable pageable);

        Page<Product> findAllByCategoryAndBrand(Category category, Brand brand, Pageable pageable);

        @Query(value = "select sum(item.quantity) from OrderItem item " +
                        "where " +
                        "item.orderShop is not null " +
                        "and item.product = :product")
        Long getSoldQuantityByProduct(Product product);

        Long countAllByShop(Shop shop);

}
