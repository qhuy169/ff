package gt.electronic.ecommerce.controller;

import gt.electronic.ecommerce.dto.request.CommentCreationDTO;
import gt.electronic.ecommerce.dto.request.CommentUpdationDTO;
import gt.electronic.ecommerce.dto.response.CommentResponseDTO;
import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.entities.Comment;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.services.CommentService;
import gt.electronic.ecommerce.utils.JwtTokenUtil;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static gt.electronic.ecommerce.utils.Utils.COMMENT_PER_PAGE;
import static gt.electronic.ecommerce.utils.Utils.DEFAULT_PAGE;

/**
 * @author quang huy
 * @created 12/09/2025 - 8:19 PM
 */
@RestController
@RequestMapping(value = "/api/v1/comments")
@CrossOrigin(origins = "*")
public class CommentController {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = Comment.class.getSimpleName();
  private CommentService commentService;

  @Autowired
  public void CommentService(CommentService commentService) {
    this.commentService = commentService;
  }

  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  public void JwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @GetMapping("/productId/{productId}")
  public ResponseObject<Page<CommentResponseDTO>> getAllCommentsByProduct(
      @PathVariable(name = "productId") Long productId,
      @RequestParam(name = "isHasChild", required = false, defaultValue = "false") Boolean isHasChild,
      @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = COMMENT_PER_PAGE) Integer size,
      @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
      @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
    isHasChild = true;
    return new ResponseObject<>(
        HttpStatus.OK, "",
        this.commentService.getAllMainCommentsByProduct(productId, isHasChild, pageable));
  }

  @GetMapping("/userId/{userId}")
  public ResponseObject<Page<CommentResponseDTO>> getAllCommentsByUser(
      @PathVariable(name = "userId") Long userId,
      @RequestParam(name = "isHasChild", required = false, defaultValue = "false") Boolean isHasChild,
      @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = COMMENT_PER_PAGE) Integer size,
      @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
      @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
    return new ResponseObject<>(
        HttpStatus.OK, "", this.commentService.getAllCommentsByUser(userId, isHasChild, pageable));
  }

  @GetMapping("/productId/{productId}/userId/{userId}")
  public ResponseObject<Page<CommentResponseDTO>> getAllCommentsByProductAndUser(
      @PathVariable(name = "productId") Long productId,
      @PathVariable(name = "userId") Long userId,
      @RequestParam(name = "isHasChild", required = false, defaultValue = "false") Boolean isHasChild,
      @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = COMMENT_PER_PAGE) Integer size,
      @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
      @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
    return new ResponseObject<>(
        HttpStatus.OK, "", this.commentService.getAllCommentsByProductAndUser(
            productId, userId, isHasChild, pageable));
  }

  @GetMapping("/{mainCommentId}/rely")
  public ResponseObject<Page<CommentResponseDTO>> getAllChildCommentsByMainComment(
      @PathVariable(name = "mainCommentId") Long mainCommentId,
      @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = COMMENT_PER_PAGE) Integer size,
      @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
      @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
    return new ResponseObject<>(
        HttpStatus.OK, "", this.commentService.getAllRelyCommentsByMainComment(mainCommentId, pageable));
  }

  @GetMapping("/{id}")
  public ResponseObject<CommentResponseDTO> getCommentById(
      @PathVariable(name = "id") Long id,
      @RequestParam(name = "isHasChild", required = false, defaultValue = "false") Boolean isHasChild) {
    return new ResponseObject<>(
        HttpStatus.OK, "", this.commentService.getCommentById(id, isHasChild));
  }

  @PostMapping(value = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @RolesAllowed({ ERole.Names.CUSTOMER, ERole.Names.SHIPPER, ERole.Names.SELLER, ERole.Names.ADMIN })
  public ResponseObject<CommentResponseDTO> createComment(
      @RequestPart(value = "data") @Valid CommentCreationDTO creationDTO,
      @RequestPart(value = "images", required = false) MultipartFile[] imageFiles,
      HttpServletRequest request) {
    // this.LOGGER.info(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(HttpStatus.CREATED, String.format(Utils.CREATE_OBJECT_SUCCESSFULLY, branchName),
        this.commentService.createComment(loginKey, creationDTO, imageFiles));
  }

  @PutMapping("/{id}")
  public ResponseObject<CommentResponseDTO> updateMainComment(
      @PathVariable(name = "id") Long id,
      @RequestPart("data") @Valid CommentUpdationDTO updationDTO,
      @RequestPart(value = "images", required = false) MultipartFile[] imageFiles) {
    return new ResponseObject<>(HttpStatus.OK, String.format(Utils.UPDATE_OBJECT_SUCCESSFULLY, branchName),
        this.commentService.updateComment(id, updationDTO, imageFiles));
  }

  // @PutMapping("/{mainCommentId}/rely/{childCommentId}")
  // public ResponseObject<CommentResponseDTO> updateRelyComment(
  // @PathVariable(name = "mainCommentId") Long mainCommentId,
  // @PathVariable(name = "childCommentId") Long childCommentId,
  // @RequestPart("data") @Valid CommentUpdationDTO updationDTO,
  // @RequestPart(value = "images", required = false) MultipartFile[] imageFiles)
  // {
  // return new ResponseObject<>(HttpStatus.OK,
  // String.format(Utils.UPDATE_OBJECT_SUCCESSFULLY, branchName),
  // this.commentService.updateComment(
  // mainCommentId, childCommentId, updationDTO, imageFiles));
  // }

  @DeleteMapping("/{id}")
  public ResponseObject<CommentResponseDTO> deleteComment(@PathVariable(name = "id") Long id) {
    return new ResponseObject<>(HttpStatus.OK, String.format(Utils.DELETE_OBJECT_SUCCESSFULLY, branchName),
        this.commentService.deleteCommentById(id));
  }
}
