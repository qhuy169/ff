package gt.electronic.ecommerce.mapper.impls;

import gt.electronic.ecommerce.dto.response.*;
import gt.electronic.ecommerce.entities.Comment;
import gt.electronic.ecommerce.entities.Feedback;
import gt.electronic.ecommerce.entities.Image;
import gt.electronic.ecommerce.mapper.FeedbackMapper;
import gt.electronic.ecommerce.mapper.UserMapper;
import gt.electronic.ecommerce.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author quang huy
 * @created 12/09/2025 - 9:07 PM
 * @project gt-backend
 */
@Component
public class FeedbackMapperImpl implements FeedbackMapper {
  private UserMapper userMapper;

  @Autowired
  public void UserMapper(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public FeedbackResponseDTO feedbackToFeedbackResponseDTO(Feedback entity, boolean... isFull) {
    if (entity == null) {
      return null;
    }
    FeedbackResponseDTO responseDTO = new FeedbackResponseDTO();
    responseDTO.setId(entity.getId());
    responseDTO.setContent(entity.getContent());
    responseDTO.setStar(entity.getStar());
    responseDTO.setSentiment(entity.getSentiment() != null ? entity.getSentiment().toString() : null);
    if (entity.getProduct() != null) {
      responseDTO.setProductId(entity.getProduct().getId());
    }
    if (entity.getAuthor() != null) {
      responseDTO.setAuthor(this.userMapper.userToUserSimpleResponseDTO(entity.getAuthor()));
    }

    if (entity.getImageGallery() != null && entity.getImageGallery().size() > 0) {
      String[] gallery = new String[entity.getImageGallery().size()];
      int i = 0;
      for (Image image : entity.getImageGallery()) {
        gallery[i] = Utils.getUrlFromPathImage(image.getPath());
        i++;
      }
      responseDTO.setImageGallery(gallery);
    }

    if (isFull.length > 0 && isFull[0]) {
      if (entity.getChildComments() != null && entity.getChildComments().size() > 0) {
        FeedbackResponseDTO[] childComments = new FeedbackResponseDTO[entity.getChildComments().size()];
        int i = 0;
        for (Comment childComment : entity.getChildComments()) {
          childComments[i] = commentToReplyForFeedbackResponseDTO(childComment);
          i++;
        }
        responseDTO.setChildFeedbacks(childComments);
      }
    }

    Utils.TimeDistance timeDistance = Utils.getTimeDistance(entity.getCreatedAt(), entity.getUpdatedAt());
    responseDTO.setTimeDistance(timeDistance.getTimeDistance());
    responseDTO.setUpdated(timeDistance.isUpdated());
    responseDTO.setCreatedAt(entity.getCreatedAt());
    responseDTO.setUpdatedAt(entity.getUpdatedAt());
    return responseDTO;
  }

  @Override
  public FeedbackResponseDTO commentToReplyForFeedbackResponseDTO(Comment entity, boolean... isFull) {
    if (entity == null) {
      return null;
    }
    FeedbackResponseDTO responseDTO = new FeedbackResponseDTO();
    if (entity.getMainFeedback() != null) {
      responseDTO.setReplyForFeedbackId(entity.getMainFeedback().getId());
    }
    responseDTO.setId(entity.getId());
    responseDTO.setAuthor(this.userMapper.userToUserSimpleResponseDTO(entity.getAuthor()));
    responseDTO.setContent(entity.getContent());
    responseDTO.setReplyForUser(this.userMapper.userToUserSimpleResponseDTO(entity.getRelyForUser()));
    Utils.TimeDistance timeDistance = Utils.getTimeDistance(entity.getCreatedAt(), entity.getUpdatedAt());
    responseDTO.setTimeDistance(timeDistance.getTimeDistance());
    responseDTO.setUpdated(timeDistance.isUpdated());
    responseDTO.setCreatedAt(entity.getCreatedAt());
    responseDTO.setUpdatedAt(entity.getUpdatedAt());
    return responseDTO;
  }
}
