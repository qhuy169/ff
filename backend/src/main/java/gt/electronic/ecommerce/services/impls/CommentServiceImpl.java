package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.dto.request.CommentCreationDTO;
import gt.electronic.ecommerce.dto.request.CommentUpdationDTO;
import gt.electronic.ecommerce.dto.response.CommentResponseDTO;
import gt.electronic.ecommerce.entities.Comment;
import gt.electronic.ecommerce.entities.Image;
import gt.electronic.ecommerce.entities.Product;
import gt.electronic.ecommerce.entities.User;
import gt.electronic.ecommerce.exceptions.ResourceAlreadyExistsException;
import gt.electronic.ecommerce.exceptions.ResourceNotFound;
import gt.electronic.ecommerce.exceptions.ResourceNotFoundException;
import gt.electronic.ecommerce.mapper.CommentMapper;
import gt.electronic.ecommerce.models.enums.ECommentType;
import gt.electronic.ecommerce.models.enums.EImageType;
import gt.electronic.ecommerce.repositories.CommentRepository;
import gt.electronic.ecommerce.repositories.ProductRepository;
import gt.electronic.ecommerce.repositories.UserRepository;
import gt.electronic.ecommerce.services.CommentService;
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
import java.util.Set;

/**
 * @author quang huy
 * @created 12/09/2025 - 8:21 PM
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = Comment.class.getSimpleName();
  private CommentMapper commentMapper;

  @Autowired
  public void CommentMapper(CommentMapper commentMapper) {
    this.commentMapper = commentMapper;
  }

  private CommentRepository commentRepo;

  @Autowired
  public void CommentRepository(CommentRepository commentRepo) {
    this.commentRepo = commentRepo;
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
  public Page<CommentResponseDTO> getAllMainCommentsByProduct(
      Long productId, boolean isHasChild, Pageable pageable) {
    this.LOGGER.info(
        String.format(Utils.LOG_GET_ALL_OBJECT_BY_FIELD, branchName,
            Product.class.getSimpleName() + "Id", productId));
    Product productFound = this.productRepo
        .findById(productId)
        .orElseThrow(
            () -> new ResourceNotFound(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    Product.class.getSimpleName(),
                    "ID",
                    productId)));
    Page<Comment> comments = this.commentRepo.getAllMainCommentByProduct(productFound, pageable);
    if (comments.getContent().size() < 1) {
      throw new ResourceNotFound(
          String.format(Utils.OBJECT_NOT_FOUND, branchName));
    }

    return comments.map(
        comment -> this.commentMapper.commentToCommentResponseDTO(comment, isHasChild));
  }

  @Override
  public Page<CommentResponseDTO> getAllCommentsByUser(
      Long userId, boolean isHasChild, Pageable pageable) {
    this.LOGGER.info(
        String.format(Utils.LOG_GET_ALL_OBJECT_BY_FIELD, branchName,
            User.class.getSimpleName() + "Id", userId));
    User userFound = this.userRepo
        .findById(userId)
        .orElseThrow(
            () -> new ResourceNotFound(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    User.class.getSimpleName(),
                    "ID",
                    userId)));
    Page<Comment> comments = this.commentRepo.getAllCommentByProductAndUser(null, userFound, pageable);
    if (comments.getContent().size() < 1) {
      throw new ResourceNotFound(
          String.format(Utils.OBJECT_NOT_FOUND, branchName));
    }
    return comments.map(
        comment -> this.commentMapper.commentToCommentResponseDTO(comment, isHasChild));
  }

  @Override
  public Page<CommentResponseDTO> getAllCommentsByProductAndUser(
      Long productId, Long userId, boolean isHasChild, Pageable pageable) {
    this.LOGGER.info(
        String.format(Utils.LOG_GET_ALL_OBJECT_BY_TWO_FIELD, branchName,
            Product.class.getSimpleName() + "Id", productId, User.class.getSimpleName() + "Id", userId));
    Product productFound = this.productRepo
        .findById(productId)
        .orElseThrow(
            () -> new ResourceNotFound(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    Product.class.getSimpleName(),
                    "ID",
                    productId)));
    User userFound = this.userRepo
        .findById(userId)
        .orElseThrow(
            () -> new ResourceNotFound(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    User.class.getSimpleName(),
                    "ID",
                    userId)));
    Page<Comment> comments = this.commentRepo.getAllCommentByProductAndUser(productFound, userFound, pageable);
    if (comments.getContent().size() < 1) {
      throw new ResourceNotFound(
          String.format(Utils.OBJECT_NOT_FOUND, branchName));
    }
    return comments.map(
        comment -> this.commentMapper.commentToCommentResponseDTO(comment, isHasChild));
  }

  @Override
  public Page<CommentResponseDTO> getAllRelyCommentsByMainComment(
      Long mainCommentId, Pageable pageable) {
    Comment mainCommentFound = this.commentRepo
        .findById(mainCommentId)
        .orElseThrow(
            () -> new ResourceNotFound(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    branchName,
                    "ID",
                    mainCommentId)));
    Page<Comment> comments = this.commentRepo.getAllChildCommentByMainComment(mainCommentFound, pageable);
    if (comments.getContent().size() < 1) {
      throw new ResourceNotFound(
          String.format(Utils.OBJECT_NOT_FOUND, branchName));
    }
    return comments.map(comment -> this.commentMapper.commentToReplyForCommentResponseDTO(comment));
  }

  @Override
  public CommentResponseDTO getCommentById(Long id, boolean isHasChild) {
    Comment commentFound = this.commentRepo
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFound(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    branchName,
                    "ID",
                    id)));
    return this.commentMapper.commentToCommentResponseDTO(commentFound, isHasChild);
  }

  @Override
  public CommentResponseDTO createComment(String loginKey, CommentCreationDTO creationDTO,
      MultipartFile[] imageGalleryFile) {
    this.LOGGER.info(
        String.format(
            Utils.LOG_CREATE_OBJECT_BY_TWO_FIELD,
            branchName,
            Product.class.getSimpleName(),
            creationDTO.getProductId(),
            "Content", creationDTO.getContent()));
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
    Comment mainComment = null;
    if (creationDTO.getReplyForCommentId() != null) {
      mainComment = this.commentRepo.findById(creationDTO.getReplyForCommentId()).orElseThrow(
          () -> new ResourceNotFoundException(
              String.format(
                  Utils.OBJECT_NOT_FOUND_BY_FIELD,
                  "Main" + branchName,
                  "ID",
                  creationDTO.getReplyForCommentId())));
    }
    Comment comment = new Comment();
    comment.setContent(creationDTO.getContent());
    comment.setProduct(productFound);
    comment.setAuthor(authorFound);
    if (creationDTO.getReplyForCommentId() != null) {
      if (mainComment.getMainComment() == null) {
        comment.setMainComment(mainComment);
      } else {
        comment.setMainComment(mainComment.getMainComment());
      }
      if (mainComment.getAuthor() != null) {
        comment.setRelyForUser(mainComment.getRelyForUser());
      }
    }

    // Set image
    if (imageGalleryFile != null && imageGalleryFile.length > 0) {
      Set<Image> imageGallery = new HashSet<>();
      for (MultipartFile multipartFile : imageGalleryFile) {
        if (!multipartFile.isEmpty()) {
          Image image = this.imageService.createImageByMultipartFile(multipartFile, EImageType.IMAGE_COMMENT);
          imageGallery.add(image);
        }
      }
      comment.setImageGallery(imageGallery);
    }
    comment.setCommentType(ECommentType.COMMENT);

    return this.commentMapper.commentToCommentResponseDTO(this.commentRepo.save(comment));
  }

  // @Override
  // public CommentResponseDTO createMainComment(
  // CommentCreationDTO creationDTO, MultipartFile[] imageGalleryFile) {
  // this.LOGGER.info(
  // String.format(
  // Utils.LOG_CREATE_OBJECT_BY_TWO_FIELD,
  // "main" + branchName,
  // Product.class.getSimpleName(),
  // creationDTO.getProductId(),
  // User.class.getSimpleName(),
  // creationDTO.getAuthorId()));
  // Product productFound =
  // this.productRepo
  // .findById(creationDTO.getProductId())
  // .orElseThrow(
  // () ->
  // new ResourceAlreadyExistsException(
  // String.format(
  // Utils.OBJECT_NOT_FOUND_BY_FIELD,
  // Product.class.getSimpleName(),
  // "ID",
  // creationDTO.getProductId())));
  // User authorFound =
  // this.userRepo
  // .findById(creationDTO.getAuthorId())
  // .orElseThrow(
  // () ->
  // new ResourceAlreadyExistsException(
  // String.format(
  // Utils.OBJECT_NOT_FOUND_BY_FIELD,
  // User.class.getSimpleName(),
  // "ID",
  // creationDTO.getAuthorId())));
  // Comment comment = new Comment();
  // comment.setContent(creationDTO.getContent());
  // comment.setProduct(productFound);
  // comment.setAuthor(authorFound);
  //
  // // Set image
  // if (imageGalleryFile != null && imageGalleryFile.length > 0) {
  // Set<Image> imageGallery = new HashSet<>();
  // for (MultipartFile multipartFile : imageGalleryFile) {
  // if (!multipartFile.isEmpty()) {
  // Image image =
  // this.imageService.createImageByMultipartFile(multipartFile,
  // EImageType.IMAGE_COMMENT);
  // imageGallery.add(image);
  // }
  // }
  // comment.setImageGallery(imageGallery);
  // }
  // comment.setCommentType(ECommentType.COMMENT);
  //
  // return ResponseEntity.status(HttpStatus.CREATED)
  // .body(
  // new ResponseObject(
  // HttpStatus.CREATED,
  // String.format(Utils.CREATE_MAIN_OBJECT_SUCCESSFULLY, branchName),
  // this.commentMapper.commentToCommentResponseDTO(this.commentRepo.save(comment))));
  // }
  //
  // @Override
  // public CommentResponseDTO createRelyComment(
  // Long relyCommentId, RelyCommentCreationDTO creationDTO, MultipartFile[]
  // imageGalleryFile) {
  // this.LOGGER.info(
  // String.format(
  // Utils.LOG_CREATE_OBJECT_BY_TWO_FIELD,
  // "Rely" + branchName,
  // "Main" + branchName,
  // relyCommentId,
  // User.class.getSimpleName(),
  // creationDTO.getAuthorId()));
  // Comment relyCommentFound =
  // this.commentRepo
  // .findById(relyCommentId)
  // .orElseThrow(
  // () ->
  // new ResourceAlreadyExistsException(
  // String.format(
  // Utils.OBJECT_NOT_FOUND_BY_FIELD,
  // branchName,
  // "ID",
  // relyCommentId)));
  // User authorFound =
  // this.userRepo
  // .findById(creationDTO.getAuthorId())
  // .orElseThrow(
  // () ->
  // new ResourceAlreadyExistsException(
  // String.format(
  // Utils.OBJECT_NOT_FOUND_BY_FIELD,
  // User.class.getSimpleName(),
  // "ID",
  // creationDTO.getAuthorId())));
  // Comment comment = new Comment();
  // comment.setContent(creationDTO.getContent());
  // comment.setProduct(relyCommentFound.getProduct());
  // comment.setAuthor(authorFound);
  // if (relyCommentFound.getMainComment() != null) {
  // comment.setMainComment(relyCommentFound.getMainComment());
  // } else {
  // comment.setMainComment(relyCommentFound);
  // }
  // comment.setRelyForUser(relyCommentFound.getAuthor());
  //
  // // Set image
  // if (imageGalleryFile != null && imageGalleryFile.length > 0) {
  // Set<Image> imageGallery = new HashSet<>();
  // for (MultipartFile multipartFile : imageGalleryFile) {
  // if (!multipartFile.isEmpty()) {
  // Image image =
  // this.imageService.createImageByMultipartFile(multipartFile,
  // EImageType.IMAGE_COMMENT);
  // imageGallery.add(image);
  // }
  // }
  // comment.setImageGallery(imageGallery);
  // }
  // comment.setCommentType(ECommentType.COMMENT);
  //
  // return ResponseEntity.status(HttpStatus.CREATED)
  // .body(
  // new ResponseObject(
  // HttpStatus.CREATED,
  // String.format(Utils.CREATE_RELY_OBJECT_SUCCESSFULLY, branchName),
  // this.commentMapper.commentToRelyCommentResponseDTO(
  // this.commentRepo.save(comment))));
  // }

  @Override
  public CommentResponseDTO updateComment(
      Long id, CommentUpdationDTO updationDTO, MultipartFile[] imageGalleryFile) {
    this.LOGGER.info(
        String.format(Utils.LOG_UPDATE_OBJECT, "Main" + branchName, "ID", id));
    Comment commentFound = this.commentRepo
        .findById(id)
        .orElseThrow(
            () -> new ResourceAlreadyExistsException(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    branchName,
                    "ID",
                    id)));

    commentFound.setContent(updationDTO.getContent());

    // update image gallery
    if (imageGalleryFile != null && imageGalleryFile.length > 0) {
      // delete old image gallery if it exists
      if (commentFound.getImageGallery() != null && commentFound.getImageGallery().size() > 0) {
        for (Image image : commentFound.getImageGallery()) {
          this.imageService.deleteImageById(image.getId());
        }
      }
      // set new image gallery
      Set<Image> imageGallery = new HashSet<>();
      for (MultipartFile multipartFile : imageGalleryFile) {
        if (!multipartFile.isEmpty()) {
          Image image = this.imageService.createImageByMultipartFile(multipartFile, EImageType.IMAGE_COMMENT);
          imageGallery.add(image);
        }
      }
      commentFound.setImageGallery(imageGallery);
    }

    return this.commentMapper.commentToCommentResponseDTO(
        this.commentRepo.save(commentFound));
  }

  // @Override
  // public CommentResponseDTO updateRelyComment(
  // Long mainCommentId,
  // Long childCommentId,
  // CommentUpdationDTO updationDTO,
  // MultipartFile[] imageGalleryFile) {
  // this.LOGGER.info(
  // String.format(
  // Utils.LOG_UPDATE_OBJECT, "Rely" + branchName, "ID", childCommentId));
  // Comment mainCommentFound =
  // this.commentRepo
  // .findById(mainCommentId)
  // .orElseThrow(
  // () ->
  // new ResourceAlreadyExistsException(
  // String.format(
  // Utils.OBJECT_NOT_FOUND_BY_FIELD,
  // branchName,
  // "ID",
  // mainCommentId)));
  //
  // Comment childCommentFound =
  // this.commentRepo
  // .findById(childCommentId)
  // .orElseThrow(
  // () ->
  // new ResourceAlreadyExistsException(
  // String.format(
  // Utils.OBJECT_NOT_FOUND_BY_FIELD,
  // branchName,
  // "ID",
  // childCommentId)));
  //
  // if (childCommentFound.getMainComment() != mainCommentFound) {
  // throw new InvalidFieldException(
  // String.format(
  // Utils.OBJECT_NOT_CHILD,
  // branchName,
  // "ID",
  // childCommentId,
  // branchName,
  // "ID",
  // mainCommentId));
  // }
  //
  // childCommentFound.setContent(updationDTO.getContent());
  //
  // // update image gallery
  // if (imageGalleryFile != null && imageGalleryFile.length > 0) {
  // // delete old image gallery if it exists
  // if (childCommentFound.getImageGallery() != null
  // && childCommentFound.getImageGallery().size() > 0) {
  // for (Image image : childCommentFound.getImageGallery()) {
  // this.imageService.deleteImageById(image.getId());
  // }
  // }
  // // set new image gallery
  // Set<Image> imageGallery = new HashSet<>();
  // for (MultipartFile multipartFile : imageGalleryFile) {
  // if (!multipartFile.isEmpty()) {
  // Image image =
  // this.imageService.createImageByMultipartFile(multipartFile,
  // EImageType.IMAGE_COMMENT);
  // imageGallery.add(image);
  // }
  // }
  // childCommentFound.setImageGallery(imageGallery);
  // }
  //
  // return this.commentMapper.commentToRelyCommentResponseDTO(
  // this.commentRepo.save(childCommentFound));
  // }

  @Override
  public CommentResponseDTO deleteCommentById(Long id) {
    this.LOGGER.info(
        String.format(Utils.LOG_DELETE_OBJECT, branchName, "ID", id));
    Comment commentFound = this.commentRepo
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    User.class.getSimpleName(),
                    "ID",
                    id)));

    // delete image gallery
    Set<Image> imageGallery = commentFound.getImageGallery();
    if (imageGallery != null && imageGallery.size() > 0) {
      for (Image image : imageGallery) {
        this.imageService.deleteImageById(image.getId());
      }
    }

    // delete Comment
    this.commentRepo.deleteById(id);
    return null;
  }
}
