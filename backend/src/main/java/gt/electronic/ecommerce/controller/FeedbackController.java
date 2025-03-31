package gt.electronic.ecommerce.controller;

import gt.electronic.ecommerce.dto.request.FeedbackCreationDTO;
import gt.electronic.ecommerce.dto.request.FeedbackUpdationDTO;
import gt.electronic.ecommerce.dto.response.FeedbackResponseDTO;
import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.entities.Feedback;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.services.FeedbackService;
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

import static gt.electronic.ecommerce.utils.Utils.DEFAULT_PAGE;
import static gt.electronic.ecommerce.utils.Utils.FEEDBACK_PER_PAGE;

/**
 * @author quang huy
 * @created 12/09/2025 - 8:19 PM
 */
@RestController
@RequestMapping(value = "/api/v1/feedbacks")
@CrossOrigin(origins = "*")
public class FeedbackController {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = Feedback.class.getSimpleName();
  private FeedbackService feedbackService;

  @Autowired
  public void FeedbackService(FeedbackService feedbackService) {
    this.feedbackService = feedbackService;
  }

  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  public void JwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @GetMapping("/productId/{productId}")
  public ResponseObject<Page<FeedbackResponseDTO>> getAllFeedbacksByProduct(
      @PathVariable(name = "productId") Long productId,
      @RequestParam(name = "isHasChild", required = false, defaultValue = "false") Boolean isHasChild,
      @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = FEEDBACK_PER_PAGE) Integer size,
      @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
      @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
    isHasChild = true;
    return new ResponseObject<>(
        HttpStatus.OK, "", this.feedbackService.getAllMainFeedbacksByProduct(productId, isHasChild, pageable));
  }

  @GetMapping("/userId/{userId}")
  public ResponseObject<Page<FeedbackResponseDTO>> getAllFeedbacksByUser(
      @PathVariable(name = "userId") Long userId,
      @RequestParam(name = "isHasChild", required = false, defaultValue = "false") Boolean isHasChild,
      @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = FEEDBACK_PER_PAGE) Integer size,
      @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
      @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
    return new ResponseObject<>(
        HttpStatus.OK, "", this.feedbackService.getAllFeedbacksByUser(userId, isHasChild, pageable));
  }

  @GetMapping("/shopId/{shopId}")
  public ResponseObject<Page<FeedbackResponseDTO>> getAllFeedbacksByShop(
      @PathVariable(name = "shopId") Long shopId,
      @RequestParam(name = "isHasChild", required = false, defaultValue = "false") Boolean isHasChild,
      @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = FEEDBACK_PER_PAGE) Integer size,
      @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
      @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
    return new ResponseObject<>(
        HttpStatus.OK, "", this.feedbackService.getAllFeedbacksByShop(shopId, isHasChild, pageable));
  }

  @GetMapping("/{mainFeedbackId}/rely")
  public ResponseObject<Page<FeedbackResponseDTO>> getAllChildFeedbacksByMainFeedback(
      @PathVariable(name = "mainFeedbackId") Long mainFeedbackId,
      @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = FEEDBACK_PER_PAGE) Integer size,
      @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
      @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
    return new ResponseObject<>(
        HttpStatus.OK, "", this.feedbackService.getAllRelyFeedbacksByMainFeedback(mainFeedbackId, pageable));
  }

  @GetMapping("/{id}")
  public ResponseObject<FeedbackResponseDTO> getFeedbackById(
      @PathVariable(name = "id") Long id,
      @RequestParam(name = "isHasChild", required = false, defaultValue = "false") Boolean isHasChild) {
    return new ResponseObject<>(
        HttpStatus.OK, "", this.feedbackService.getFeedbackById(id, isHasChild));
  }

  @GetMapping("/productId/{productId}/userId/{userId}")
  public ResponseObject<FeedbackResponseDTO> getFeedbackByProductAndUser(
      @PathVariable(name = "productId") Long productId,
      @PathVariable(name = "userId") Long userId,
      @RequestParam(name = "isHasChild", required = false, defaultValue = "false") Boolean isHasChild) {
    return new ResponseObject<>(
        HttpStatus.OK, "", this.feedbackService.getFeedbackByProductAndUser(productId, userId, isHasChild));
  }

  @PostMapping(value = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @RolesAllowed({ ERole.Names.CUSTOMER, ERole.Names.SHIPPER, ERole.Names.SELLER, ERole.Names.ADMIN })
  public ResponseObject<FeedbackResponseDTO> createFeedback(
      @RequestPart("data") @Valid FeedbackCreationDTO creationDTO,
      @RequestPart(value = "images", required = false) MultipartFile[] imageFiles,
      HttpServletRequest request) {
    // this.LOGGER.info(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(HttpStatus.CREATED, String.format(Utils.CREATE_OBJECT_SUCCESSFULLY, branchName),
        this.feedbackService.createFeedback(loginKey, creationDTO, imageFiles));
  }

  // @PostMapping("/{mainFeedbackId}/rely")
  // public > createRelyFeedback(
  // @PathVariable(name = "mainFeedbackId") Long relyFeedbackId,
  // @RequestPart("data") @Valid RelyFeedbackCreationDTO creationDTO) {
  // return this.feedbackService.createRelyFeedback(relyFeedbackId, creationDTO);
  // }

  @PutMapping("/{feedbackId}")
  public ResponseObject<FeedbackResponseDTO> updateMainFeedback(
      @PathVariable(name = "feedbackId") Long id,
      @RequestPart("data") @Valid FeedbackUpdationDTO updationDTO,
      @RequestPart(value = "images", required = false) MultipartFile[] imageFiles) {
    return new ResponseObject<>(HttpStatus.OK, String.format(Utils.UPDATE_OBJECT_SUCCESSFULLY, branchName),
        this.feedbackService.updateFeedback(id, updationDTO, imageFiles));
  }

  @DeleteMapping("/{feedbackId}")
  public ResponseObject<FeedbackResponseDTO> deleteFeedback(@PathVariable(name = "feedbackId") Long id) {
    return new ResponseObject<>(HttpStatus.OK, String.format(Utils.DELETE_OBJECT_SUCCESSFULLY, branchName),
        this.feedbackService.deleteFeedbackById(id));
  }
}
