package gt.electronic.ecommerce.dto.request;

import gt.electronic.ecommerce.entities.Product;
import gt.electronic.ecommerce.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author quang huy
 * @created 12/09/2025 - 8:07 PM
 * @project gt-backend
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackCreationDTO {
  private Long productId;
  private Long authorId;
  private String content;
  private int star;
  private Long replyForFeedbackId;
  private String sentiment;
}
