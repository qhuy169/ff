package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.Comment;
import gt.electronic.ecommerce.entities.Feedback;
import gt.electronic.ecommerce.entities.Product;
import gt.electronic.ecommerce.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author quang huy
 * @created 12/09/2025 - 8:56 PM
 * @project gt-backend
 */
@Repository
@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {

        @Query(value = "select c from Comment c where c.commentType = gt.electronic.ecommerce.models.enums.ECommentType.COMMENT "
                        + "and c.mainComment is null "
                        + "and c.product = :product "
                        + "order by c.createdAt desc")
        Page<Comment> getAllMainCommentByProduct(Product product, Pageable pageable);

        @Query(value = "select c from Comment c where c.commentType = gt.electronic.ecommerce.models.enums.ECommentType.COMMENT "
                        + "and (?1 is null or c.product = :product) "
                        + "and (?2 is null or c.author = :author) "
                        + "order by c.createdAt asc")
        Page<Comment> getAllCommentByProductAndUser(Product product, User author, Pageable pageable);

        @Query(value = "select c from Comment c where c.commentType = gt.electronic.ecommerce.models.enums.ECommentType.COMMENT "
                        + "and c.mainComment = :comment order by c.createdAt asc")
        Page<Comment> getAllChildCommentByMainComment(Comment comment, Pageable pageable);

        @Query(value = "select c from Comment c where c.commentType = gt.electronic.ecommerce.models.enums.ECommentType.FEEDBACK "
                        + "and c.mainFeedback = :feedback order by c.createdAt asc")
        Page<Comment> getAllChildFeedbackByMainFeedback(Feedback feedback, Pageable pageable);
}
