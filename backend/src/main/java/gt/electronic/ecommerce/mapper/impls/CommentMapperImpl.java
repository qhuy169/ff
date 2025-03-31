package gt.electronic.ecommerce.mapper.impls;

import gt.electronic.ecommerce.dto.response.CommentResponseDTO;
import gt.electronic.ecommerce.entities.Comment;
import gt.electronic.ecommerce.entities.Image;
import gt.electronic.ecommerce.mapper.CommentMapper;
import gt.electronic.ecommerce.mapper.UserMapper;
import gt.electronic.ecommerce.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author quang huy
 * @created 12/09/2025 - 9:06 PM
 * @project gt-backend
 */
@Component
public class CommentMapperImpl implements CommentMapper {
  private UserMapper userMapper;

  @Autowired
  public void UserMapper(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public CommentResponseDTO commentToCommentResponseDTO(Comment entity, boolean... isFull) {
    if (entity == null) {
      return null;
    }
    CommentResponseDTO responseDTO = new CommentResponseDTO();
    responseDTO.setId(entity.getId());
    responseDTO.setContent(entity.getContent());
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
        CommentResponseDTO[] childComments = new CommentResponseDTO[entity.getChildComments().size()];
        int i = 0;
        for (Comment childComment : entity.getChildComments()) {
          childComments[i] = commentToReplyForCommentResponseDTO(childComment);
          i++;
        }
        responseDTO.setChildComments(childComments);
      }
    }

    Utils.TimeDistance timeDistance = Utils.getTimeDistance(entity.getCreatedAt(), entity.getUpdatedAt());
    responseDTO.setTimeDistance(timeDistance.getTimeDistance());
    responseDTO.setUpdated(timeDistance.isUpdated());
    return responseDTO;
  }

  @Override
  public CommentResponseDTO commentToReplyForCommentResponseDTO(Comment entity, boolean... isFull) {
    if (entity == null) {
      return null;
    }
    CommentResponseDTO responseDTO = new CommentResponseDTO();
    if (entity.getMainComment() != null) {
      responseDTO.setReplyForCommentId(entity.getMainComment().getId());
    }
    responseDTO.setId(entity.getId());
    responseDTO.setAuthor(this.userMapper.userToUserSimpleResponseDTO(entity.getAuthor()));
    responseDTO.setContent(entity.getContent());
    responseDTO.setReplyForUser(this.userMapper.userToUserSimpleResponseDTO(entity.getRelyForUser()));

    if (entity.getImageGallery() != null && entity.getImageGallery().size() > 0) {
      String[] gallery = new String[entity.getImageGallery().size()];
      int j = 0;
      for (Image image : entity.getImageGallery()) {
        gallery[j] = Utils.getUrlFromPathImage(image.getPath());
        j++;
      }
      responseDTO.setImageGallery(gallery);
    }

    Utils.TimeDistance timeDistance = Utils.getTimeDistance(entity.getCreatedAt(), entity.getUpdatedAt());
    responseDTO.setTimeDistance(timeDistance.getTimeDistance());
    responseDTO.setUpdated(timeDistance.isUpdated());
    return responseDTO;
  }
}
