package gt.electronic.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author quang huy
 * @created 12/09/2025 - 8:08 PM
 * @project gt-backend
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
  private Long replyForCommentId;
  private Long id;
  private Long productId;
  private UserSimpleResponseDTO author;
  private String content;
  private CommentResponseDTO[] childComments;
  private UserSimpleResponseDTO replyForUser;
  private String[] imageGallery;
  private String timeDistance;
  private boolean isUpdated;
}
