package gt.electronic.ecommerce.dto.request;

import lombok.*;

/**
 * @author quang huy
 * @created 12/09/2025 - 8:07 PM
 * @project gt-backend
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreationDTO {
  private Long productId;
  private Long authorId;
  private String content;
  private Long replyForCommentId;
}
