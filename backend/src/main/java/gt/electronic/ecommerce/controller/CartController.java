package gt.electronic.ecommerce.controller;

import gt.electronic.ecommerce.dto.request.CartDetailCreationDTO;
import gt.electronic.ecommerce.dto.request.CartItemCreationDTO;
import gt.electronic.ecommerce.dto.response.CartItemResponseDTO;
import gt.electronic.ecommerce.dto.response.OrderDetailResponseDTO;
import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.services.CartItemService;
import gt.electronic.ecommerce.utils.JwtTokenUtil;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static gt.electronic.ecommerce.utils.Utils.DEFAULT_PAGE;
import static gt.electronic.ecommerce.utils.Utils.DEFAULT_SIZE;

/**
 * @author quang huy
 * @created 19/09/2025 - 3:22 PM
 * @project gt-backend
 */
@RestController
@RequestMapping("/api/v1/carts")
@CrossOrigin(origins = "*")
public class CartController {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = "Cart Item";
  private CartItemService cartItemService;

  @Autowired
  public void CartItemService(CartItemService cartItemService) {
    this.cartItemService = cartItemService;
  }

  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  public void JwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @GetMapping
  @RolesAllowed({ ERole.Names.CUSTOMER, ERole.Names.SHIPPER, ERole.Names.SELLER, ERole.Names.ADMIN })
  public List<OrderDetailResponseDTO> getAllCartItemsByUser(
      @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = DEFAULT_SIZE) Integer size,
      @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
      @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
      @RequestParam(name = "userId", required = false, defaultValue = "0") Long userId,
      HttpServletRequest request) {
    // this.LOGGER.info(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
    return this.cartItemService.getAllCartItemByUser(loginKey, userId, pageable);
  }

  @GetMapping("/{id}")
  @RolesAllowed({ ERole.Names.CUSTOMER, ERole.Names.SHIPPER, ERole.Names.SELLER, ERole.Names.ADMIN })
  public OrderDetailResponseDTO getCartItemById(@PathVariable(name = "id") Long id) {
    return this.cartItemService.getCartItemById(id);
  }

  @PostMapping
  @RolesAllowed({ ERole.Names.CUSTOMER, ERole.Names.SHIPPER, ERole.Names.SELLER, ERole.Names.ADMIN })
  public ResponseObject<OrderDetailResponseDTO> createCartItem(
      @RequestBody CartItemCreationDTO creationDTO,
      HttpServletRequest request) {
    // this.LOGGER.info(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(HttpStatus.CREATED, String.format(Utils.CREATE_OBJECT_SUCCESSFULLY, branchName),
        this.cartItemService.createCartItem(loginKey, creationDTO));
  }

  @PostMapping("/addAll")
  @RolesAllowed({ ERole.Names.CUSTOMER, ERole.Names.SHIPPER, ERole.Names.SELLER, ERole.Names.ADMIN })
  public ResponseObject<List<OrderDetailResponseDTO>> updateAllCartItems(
      @RequestBody List<CartDetailCreationDTO> creationDTOList,
      HttpServletRequest request) {
    // this.LOGGER.info(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(HttpStatus.OK, String.format(Utils.UPDATE_OBJECT_SUCCESSFULLY, branchName),
        this.cartItemService.updateCart(loginKey, creationDTOList));
  }

  @PutMapping("/{id}")
  @RolesAllowed({ ERole.Names.CUSTOMER, ERole.Names.SHIPPER, ERole.Names.SELLER, ERole.Names.ADMIN })
  public ResponseObject<OrderDetailResponseDTO> updateCartItem(
      @PathVariable(name = "id") Long id,
      @RequestPart("data") @Valid CartItemCreationDTO creationDTO,
      HttpServletRequest request) {
    // this.LOGGER.info(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(HttpStatus.OK, String.format(Utils.UPDATE_OBJECT_SUCCESSFULLY, branchName),
        this.cartItemService.updateCartItem(loginKey, id, creationDTO));
  }

  @DeleteMapping("/{id}")
  @RolesAllowed({ ERole.Names.CUSTOMER, ERole.Names.SHIPPER, ERole.Names.SELLER, ERole.Names.ADMIN })
  public ResponseObject<OrderDetailResponseDTO> deleteCartItem(
      @PathVariable(name = "id") Long id,
      @RequestParam(name = "userId", required = false, defaultValue = "0") Long userId,
      HttpServletRequest request) {
    // this.LOGGER.info(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(HttpStatus.OK, String.format(Utils.DELETE_OBJECT_SUCCESSFULLY, branchName),
        this.cartItemService.deleteCartItemById(loginKey, userId, id));
  }
}
