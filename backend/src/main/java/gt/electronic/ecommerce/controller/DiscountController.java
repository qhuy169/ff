package gt.electronic.ecommerce.controller;

import gt.electronic.ecommerce.dto.request.DiscountCreationDTO;
import gt.electronic.ecommerce.dto.request.DiscountUpdateDTO;
import gt.electronic.ecommerce.dto.response.DiscountResponseDTO;
import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.entities.Discount;
import gt.electronic.ecommerce.models.clazzs.DiscountCodes;
import gt.electronic.ecommerce.services.DiscountService;
import gt.electronic.ecommerce.utils.JwtTokenUtil;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author quang huy
 * @created 25/11/2025 - 8:49 AM
 */
@RestController
@RequestMapping(value = "/api/v1/discounts")
@CrossOrigin(origins = "*")
public class DiscountController {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = Discount.class.getSimpleName();
  private DiscountService discountService;

  @Autowired
  public void DiscountService(DiscountService discountService) {
    this.discountService = discountService;
  }

  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  public void JwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @GetMapping("/search")
  public ResponseObject<List<DiscountResponseDTO>> getSearchDiscount(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_SIZE) int size,
      @RequestParam(name = "title", required = false) String title,
      @RequestParam(name = "percent", required = false) Double percent,
      @RequestParam(name = "fromPercent", required = false) Double fromPercent,
      @RequestParam(name = "toPercent", required = false) Double toPercent,
      @RequestParam(name = "code", required = false) String code,
      @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
      @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
      @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
      @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
      @RequestParam(name = "shopId", required = false) Long shopId) {
    Pageable pageable = PageRequest.of(page - 1, size);
    System.out.println(title + " | " + percent + " | " + code + " | " + startDate + " | " + endDate);
    return new ResponseObject<>(
        HttpStatus.OK, "", this.discountService.searchDiscount(title, percent, code, startDate, endDate, fromPercent,
            toPercent, fromDate, toDate, shopId, pageable).toList());
  }

  @GetMapping("/user")
  public ResponseObject<List<DiscountResponseDTO>> getDiscountByUser(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_SIZE) int size,
      HttpServletRequest request) {
    Pageable pageable = PageRequest.of(page - 1, size);
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(
        HttpStatus.OK, "", this.discountService.getAllDiscountByUser(loginKey, pageable));
  }

  @GetMapping("/{id}")
  public ResponseObject<DiscountResponseDTO> getDiscountById(@PathVariable(name = "id") Long id) {
    return new ResponseObject<>(
        HttpStatus.OK, "", this.discountService.getDiscountById(id));
  }

  @GetMapping("/check-code/{code}")
  public ResponseObject<DiscountResponseDTO> checkDiscountByCode(
      @PathVariable(name = "code") String code,
      HttpServletRequest request) {
    String loginKey = "";
    try {
      loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    } catch (Exception e) {
      loginKey = "";
    }
    return new ResponseObject<>(
        HttpStatus.OK, "", this.discountService.checkDiscountByCode(loginKey, code));
  }

  @PostMapping
  public ResponseObject<DiscountResponseDTO> createDiscount(
      @RequestPart("data") @Valid DiscountCreationDTO discountCreationDTO,
      @RequestPart(value = "image", required = false) MultipartFile imageFile,
      HttpServletRequest request) {
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(HttpStatus.CREATED, String.format(Utils.CREATE_OBJECT_SUCCESSFULLY, branchName),
        this.discountService.createDiscount(loginKey, discountCreationDTO, imageFile));
  }

  @PutMapping("/{id}")
  public ResponseObject<DiscountResponseDTO> updateDiscount(
      @PathVariable(name = "id") Long id,
      @RequestPart("data") @Valid DiscountUpdateDTO discountUpdateDTO,
      @RequestPart(value = "image", required = false) MultipartFile imageFile,
      HttpServletRequest request) {
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(HttpStatus.OK, String.format(Utils.UPDATE_OBJECT_SUCCESSFULLY, branchName),
        this.discountService.updateDiscount(loginKey, id, discountUpdateDTO, imageFile));
  }

  @DeleteMapping("/{id}")
  public ResponseObject<DiscountResponseDTO> deleteDiscount(
      @PathVariable(name = "id") Long id,
      HttpServletRequest request) {
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(HttpStatus.OK, String.format(Utils.DELETE_OBJECT_SUCCESSFULLY, branchName),
        this.discountService.deleteDiscountById(loginKey, id));
  }

  @PutMapping("/user/add-all")
  public ResponseObject<List<DiscountResponseDTO>> addAllDiscount(
      @RequestBody DiscountCodes discountCodes,
      HttpServletRequest request) {
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(HttpStatus.OK,
        String.format(Utils.ADD_ALL_OBJECT_TO_OBJECT_SUCCESSFULLY, "discountCodes", branchName),
        this.discountService.addAllDiscountToUser(loginKey, discountCodes));
  }

  @PutMapping("/user/remove-all")
  public ResponseObject<List<DiscountResponseDTO>> removeAllDiscount(
      @RequestBody DiscountCodes discountCodes,
      HttpServletRequest request) {
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(HttpStatus.OK,
        String.format(Utils.REMOVE_ALL_OBJECT_FROM_OBJECT_SUCCESSFULLY,
            "discountCodes",
            branchName),
        this.discountService.removeAllDiscountFromUser(loginKey, discountCodes));
  }
}
