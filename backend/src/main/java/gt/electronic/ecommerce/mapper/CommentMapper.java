package gt.electronic.ecommerce.mapper;

import gt.electronic.ecommerce.dto.response.CommentResponseDTO;
import gt.electronic.ecommerce.entities.Comment;

/**
 * @author quang huy
 * @created 12/09/2025 - 9:06 PM
 * @project gt-backend
 */
public interface CommentMapper {
  CommentResponseDTO commentToCommentResponseDTO(Comment entity, boolean... isFull);

  CommentResponseDTO commentToReplyForCommentResponseDTO(Comment entity, boolean... isFull);
}
