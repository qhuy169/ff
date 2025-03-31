package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.dto.request.FeedbackCreationDTO;
import gt.electronic.ecommerce.dto.request.FeedbackUpdationDTO;
import gt.electronic.ecommerce.dto.response.FeedbackResponseDTO;
import gt.electronic.ecommerce.models.clazzs.ProductRating;
import gt.electronic.ecommerce.models.clazzs.ProductSentiment;
import gt.electronic.ecommerce.models.clazzs.SentimentDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author quang huy
 * @created 12/09/2025 - 8:21 PM
 */
public interface FeedbackService {
  Page<FeedbackResponseDTO> getAllMainFeedbacksByProduct(Long productId, boolean isHasChild, Pageable pageable);

  Page<FeedbackResponseDTO> getAllFeedbacksByUser(Long userId, boolean isHasChild, Pageable pageable);

  Page<FeedbackResponseDTO> getAllFeedbacksByShop(Long shopId, boolean isHasChild, Pageable pageable);

  Page<FeedbackResponseDTO> getAllRelyFeedbacksByMainFeedback(Long mainFeedbackId, Pageable pageable);

  FeedbackResponseDTO getFeedbackById(Long id, boolean isHasChild);

  FeedbackResponseDTO getFeedbackByProductAndUser(Long productId, Long userId, boolean isHasChild);

  ProductRating getProductRatingByProduct(Long productId);

  ProductSentiment getProductSentimentByProduct(Long productId);

  FeedbackResponseDTO createFeedback(String loginKey, FeedbackCreationDTO creationDTO,
      MultipartFile[] imageGalleryFile);

  FeedbackResponseDTO updateFeedback(Long id, FeedbackUpdationDTO updationDTO, MultipartFile[] imageGalleryFile);

  FeedbackResponseDTO deleteFeedbackById(Long id);
}
