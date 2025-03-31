package gt.electronic.ecommerce.mapper;

import gt.electronic.ecommerce.dto.response.FeedbackResponseDTO;
import gt.electronic.ecommerce.entities.Comment;
import gt.electronic.ecommerce.entities.Feedback;

/**
 * @author quang huy
 * @created 12/09/2025 - 9:07 PM
 * @project gt-backend
 */
public interface FeedbackMapper {
  FeedbackResponseDTO feedbackToFeedbackResponseDTO(Feedback entity, boolean... isFull);

  FeedbackResponseDTO commentToReplyForFeedbackResponseDTO(Comment entity, boolean... isFull);
}
