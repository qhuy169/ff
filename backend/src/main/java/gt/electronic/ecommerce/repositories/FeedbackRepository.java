package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.Feedback;
import gt.electronic.ecommerce.entities.Product;
import gt.electronic.ecommerce.entities.Shop;
import gt.electronic.ecommerce.entities.User;
import gt.electronic.ecommerce.models.interfaces.IInfoRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author quang huy
 * @created 12/09/2025 - 8:56 PM
 * @project gt-backend
 */
@Repository
@Transactional
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
        @Query(value = "select fb from Feedback fb where "
                        + "(:shop is null or fb.product.shop = :shop) "
                        + "and (:product is null or fb.product = :product) "
                        + "and (:author is null or fb.author = :author) "
                        + "order by fb.createdAt desc")
        Page<Feedback> getAllMainFeedbackByShopOrProductOrUser(Shop shop, Product product, User author,
                        Pageable pageable);

        @Query(value = "select fb from Feedback fb where "
                        + "(:product is null or fb.product = :product) "
                        + "and (:author is null or fb.author = :author) "
                        + "order by fb.createdAt asc")
        List<Feedback> getFeedbackByProductAndUser(Product product, User author);

        List<Feedback> findAllByProduct(Product product);

        @Query(value = "select count(fb) as totalVote, fb.star as star " +
                        "from Feedback fb where " +
                        "fb.product.shop = :shop " +
                        "group by fb.star")
        List<IInfoRating> getAllInfoRatingByShop(@Param("shop") Shop shop);
}
