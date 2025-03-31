package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.dto.request.FeedbackCreationDTO;
import gt.electronic.ecommerce.dto.request.FeedbackUpdationDTO;
import gt.electronic.ecommerce.dto.response.FeedbackResponseDTO;
import gt.electronic.ecommerce.entities.*;
import gt.electronic.ecommerce.exceptions.ResourceAlreadyExistsException;
import gt.electronic.ecommerce.exceptions.ResourceNotFound;
import gt.electronic.ecommerce.exceptions.ResourceNotFoundException;
import gt.electronic.ecommerce.mapper.FeedbackMapper;
import gt.electronic.ecommerce.models.clazzs.ProductRating;
import gt.electronic.ecommerce.models.clazzs.ProductSentiment;
import gt.electronic.ecommerce.models.clazzs.SentimentDetail;
import gt.electronic.ecommerce.models.enums.ECommentType;
import gt.electronic.ecommerce.models.enums.EImageType;
import gt.electronic.ecommerce.models.enums.ESentiment;
import gt.electronic.ecommerce.repositories.*;
import gt.electronic.ecommerce.services.FeedbackService;
import gt.electronic.ecommerce.services.ImageService;
import gt.electronic.ecommerce.services.UserService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author quang huy
 * @created 12/09/2025 - 8:21 PM
 */
@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String branchName = Feedback.class.getSimpleName();

    private CommentRepository commentRepo;

    @Autowired
    public void CommentRepository(CommentRepository commentRepo) {
        this.commentRepo = commentRepo;
    }

    private FeedbackMapper feedbackMapper;

    @Autowired
    public void FeedbackMapper(FeedbackMapper feedbackMapper) {
        this.feedbackMapper = feedbackMapper;
    }

    private FeedbackRepository feedbackRepo;

    @Autowired
    public void FeedbackRepository(FeedbackRepository feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    private ImageService imageService;

    @Autowired
    public void ImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    private ProductRepository productRepo;

    @Autowired
    public void ProductRepository(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    private ShopRepository shopRepo;

    @Autowired
    public void ShopRepository(ShopRepository shopRepo) {
        this.shopRepo = shopRepo;
    }

    private UserRepository userRepo;

    @Autowired
    public void UserRepository(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    private UserService userService;

    @Autowired
    public void UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Page<FeedbackResponseDTO> getAllMainFeedbacksByProduct(
            Long productId, boolean isHasChild,
            Pageable pageable) {
        Product productFound = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFound(
                String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, Product.class.getSimpleName(), "ID", productId)));
        Page<Feedback> feedbacks = this.feedbackRepo.getAllMainFeedbackByShopOrProductOrUser(null, productFound, null,
                pageable);
        if (feedbacks.getContent().size() < 1) {
            throw new ResourceNotFound(
                    String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, branchName, "ProductId",
                            productId));
        }
        return feedbacks.map(feedback -> this.feedbackMapper.feedbackToFeedbackResponseDTO(feedback, isHasChild));
    }

    @Override
    public Page<FeedbackResponseDTO> getAllFeedbacksByUser(Long userId, boolean isHasChild, Pageable pageable) {
        User userFound = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFound(
                String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, User.class.getSimpleName(), "ID", userId)));
        Page<Feedback> feedbacks = this.feedbackRepo.getAllMainFeedbackByShopOrProductOrUser(null, null, userFound,
                pageable);
        if (feedbacks.getContent().size() < 1) {
            throw new ResourceNotFound(
                    String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, branchName, "UserId",
                            userId));
        }
        return feedbacks.map(feedback -> this.feedbackMapper.feedbackToFeedbackResponseDTO(feedback, isHasChild));
    }

    @Override
    public Page<FeedbackResponseDTO> getAllFeedbacksByShop(Long shopId, boolean isHasChild, Pageable pageable) {
        this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT_BY_FIELD, branchName, "ShopId", shopId));
        Shop shopFound = this.shopRepo.findById(shopId).orElseThrow(() -> new ResourceNotFound(
                String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, Shop.class.getSimpleName(), "ID", shopId)));
        Page<Feedback> feedbacks = this.feedbackRepo.getAllMainFeedbackByShopOrProductOrUser(shopFound, null, null,
                pageable);
        if (feedbacks.getContent().size() < 1) {
            throw new ResourceNotFound(
                    String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, branchName, "ShopId",
                            shopId));
        }
        return feedbacks.map(feedback -> this.feedbackMapper.feedbackToFeedbackResponseDTO(feedback, isHasChild));
    }

    @Override
    public Page<FeedbackResponseDTO> getAllRelyFeedbacksByMainFeedback(
            Long mainFeedbackId,
            Pageable pageable) {
        Feedback mainFeedbackFound = this.feedbackRepo.findById(mainFeedbackId).orElseThrow(
                () -> new ResourceNotFound(
                        String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, branchName, "ID", mainFeedbackId)));
        Page<Comment> comments = this.commentRepo.getAllChildFeedbackByMainFeedback(mainFeedbackFound, pageable);
        if (comments.getContent().size() < 1) {
            throw new ResourceNotFound(
                    String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, Comment.class.getSimpleName(), branchName,
                            mainFeedbackId));
        }
        return comments.map(comment -> this.feedbackMapper.commentToReplyForFeedbackResponseDTO(comment));
    }

    @Override
    public FeedbackResponseDTO getFeedbackById(Long id, boolean isHasChild) {
        Feedback feedbackFound = this.feedbackRepo.findById(id).orElseThrow(() -> new ResourceNotFound(
                String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, branchName, "ID", id)));
        return this.feedbackMapper.feedbackToFeedbackResponseDTO(feedbackFound, isHasChild);
    }

    @Override
    public FeedbackResponseDTO getFeedbackByProductAndUser(Long productId, Long userId, boolean isHasChild) {
        Product productFound = this.productRepo.findById(productId).orElseThrow(() -> new ResourceNotFound(
                String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, Product.class.getSimpleName(), "ID", productId)));
        User userFound = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFound(
                String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, User.class.getSimpleName(), "ID", userId)));
        List<Feedback> feedbacks = this.feedbackRepo.getFeedbackByProductAndUser(productFound, userFound);
        if (feedbacks.size() < 1) {
            throw new ResourceNotFound(
                    String.format(Utils.OBJECT_NOT_FOUND_BY_TWO_FIELD, branchName,
                            Product.class.getSimpleName(), productId, User.class.getSimpleName(), userId));
        }
        return this.feedbackMapper.feedbackToFeedbackResponseDTO(feedbacks.get(0), isHasChild);
    }

    @Override
    public ProductRating getProductRatingByProduct(Long productId) {
        Product productFound = this.productRepo.findById(productId).orElse(null);
        if (productFound != null) {
            List<Feedback> feedbackList = this.feedbackRepo.findAllByProduct(productFound);
            if (!feedbackList.isEmpty()) {
                double star = 0;
                long[] totalVotes = new long[] { 0, 0, 0, 0, 0, 0, 0 };
                for (Feedback feedback : feedbackList) {
                    if (feedback.getStar() > 0 && feedback.getStar() < 6) {
                        star += feedback.getStar();
                        totalVotes[0]++;
                        totalVotes[feedback.getStar()]++;
                    } else if (feedback.getStar() == -1) {
                        totalVotes[6]++;
                    }
                }

                return new ProductRating(productId, star / totalVotes[0], totalVotes);
            } else {
                return new ProductRating(productId, 0, null);
            }
        }
        return null;
    }

    @Override
    public ProductSentiment getProductSentimentByProduct(Long productId) {
        Product productFound = this.productRepo.findById(productId).orElse(null);
        if (productFound != null) {
            List<Feedback> feedbackList = this.feedbackRepo.findAllByProduct(productFound);
            if (!feedbackList.isEmpty()) {
                long[] totalSentiments = new long[] { 0, 0, 0, 0 };
                for (Feedback feedback : feedbackList) {
                    totalSentiments[0]++;
                    if (feedback.getSentiment() != null) {
                        totalSentiments[feedback.getSentiment().ordinal() + 1]++;
                    } else {
                        totalSentiments[ESentiment.SENTIMENT_NEUTRAL.ordinal() + 1]++;
                    }
                }
                return new ProductSentiment(productId, totalSentiments);
            } else {
                return new ProductSentiment(productId, null);
            }
        }
        return null;
    }

    @Override
    public FeedbackResponseDTO createFeedback(
            String loginKey, FeedbackCreationDTO creationDTO,
            MultipartFile[] imageGalleryFile) {
        this.LOGGER.info(String.format(Utils.LOG_CREATE_OBJECT,
                branchName,
                Product.class.getSimpleName(),
                creationDTO.getProductId(),
                "Content",
                creationDTO.getContent()));
        Product productFound = this.productRepo
                .findById(creationDTO.getProductId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format(
                                        Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                        Product.class.getSimpleName(),
                                        "ID",
                                        creationDTO.getProductId())));
        User authorFound = this.userService.getUserByLoginKey(loginKey);
        Feedback mainFeedbackFound = null;
        if (creationDTO.getReplyForFeedbackId() != null) {
            mainFeedbackFound = this.feedbackRepo.findById(creationDTO.getReplyForFeedbackId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format(
                                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                    "Main" + branchName,
                                    "ID",
                                    creationDTO.getReplyForFeedbackId())));
        }
        if (mainFeedbackFound == null) {
            List<Feedback> feedbacks = this.feedbackRepo.getFeedbackByProductAndUser(productFound, authorFound);
            if (feedbacks.size() > 0) {
                throw new ResourceAlreadyExistsException(
                        String.format(Utils.OBJECT_EXISTED_BY_TWO_FIELD, branchName,
                                Product.class.getSimpleName(), creationDTO.getProductId(),
                                User.class.getSimpleName(),
                                authorFound.getUsername()));
            }
            // Create main feedback
            Feedback feedback = new Feedback();
            feedback.setContent(creationDTO.getContent());
            feedback.setStar(creationDTO.getStar());
            feedback.setProduct(productFound);
            feedback.setAuthor(authorFound);
            feedback.setSentiment(
                    creationDTO.getSentiment() != null ? ESentiment.getESentimentFromString(creationDTO.getSentiment())
                            : null);

            // Set image
            if (imageGalleryFile != null && imageGalleryFile.length > 0) {
                Set<Image> imageGallery = new HashSet<>();
                for (MultipartFile multipartFile : imageGalleryFile) {
                    if (!multipartFile.isEmpty()) {
                        Image image = this.imageService.createImageByMultipartFile(multipartFile,
                                EImageType.IMAGE_FEEDBACK);
                        imageGallery.add(image);
                    }
                }
                feedback.setImageGallery(imageGallery);
            }
            return this.feedbackMapper.feedbackToFeedbackResponseDTO(this.feedbackRepo.save(feedback));
        } else {
            // Create child Feedback
            Comment comment = new Comment();
            comment.setContent(creationDTO.getContent());
            comment.setProduct(productFound);
            comment.setAuthor(authorFound);
            comment.setRelyForUser(mainFeedbackFound.getAuthor());
            comment.setMainFeedback(mainFeedbackFound);
            comment.setCommentType(ECommentType.FEEDBACK);
            return this.feedbackMapper.commentToReplyForFeedbackResponseDTO(this.commentRepo.save(comment));
        }
    }

    @Override
    public FeedbackResponseDTO updateFeedback(
            Long id, FeedbackUpdationDTO updationDTO,
            MultipartFile[] imageGalleryFile) {
        this.LOGGER.info(String.format(Utils.LOG_UPDATE_OBJECT, "Main" + branchName, "ID", id));
        Feedback feedbackFound = this.feedbackRepo.findById(id).orElseThrow(() -> new ResourceAlreadyExistsException(
                String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, branchName, "ID", id)));

        feedbackFound.setContent(updationDTO.getContent());
        feedbackFound.setStar(updationDTO.getStar());

        // update image gallery
        if (imageGalleryFile != null && imageGalleryFile.length > 0) {
            // delete old image gallery if it exists
            if (feedbackFound.getImageGallery() != null && feedbackFound.getImageGallery().size() > 0) {
                for (Image image : feedbackFound.getImageGallery()) {
                    this.imageService.deleteImageById(image.getId());
                }
            }
            // set new image gallery
            Set<Image> imageGallery = new HashSet<>();
            for (MultipartFile multipartFile : imageGalleryFile) {
                if (!multipartFile.isEmpty()) {
                    Image image = this.imageService.createImageByMultipartFile(multipartFile,
                            EImageType.IMAGE_PRODUCT_GALLERY);
                    imageGallery.add(image);
                }
            }
            feedbackFound.setImageGallery(imageGallery);
        }

        return this.feedbackMapper.feedbackToFeedbackResponseDTO(this.feedbackRepo.save(feedbackFound));
    }

    @Override
    public FeedbackResponseDTO deleteFeedbackById(Long id) {
        this.LOGGER.info(String.format(Utils.LOG_DELETE_OBJECT, branchName, "ID", id));
        this.feedbackRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, User.class.getSimpleName(), "ID", id)));

        // delete Feedback
        this.feedbackRepo.deleteById(id);
        return null;
    }
}
