package gt.electronic.ecommerce.controller;

import gt.electronic.ecommerce.dto.request.OrderUpdateStatusDTO;
import gt.electronic.ecommerce.dto.response.OrderResponseDTO;
import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.entities.Order;
import gt.electronic.ecommerce.entities.OrderShop;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.services.OrderService;
import gt.electronic.ecommerce.services.OrderShopService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static gt.electronic.ecommerce.utils.Utils.DEFAULT_PAGE;
import static gt.electronic.ecommerce.utils.Utils.DEFAULT_SIZE;

/**
 * @author quang huy
 * @created 23/12/2025 - 7:40 PM
 */
@RestController
@RequestMapping(value = "/api/v1/ordershops")
@CrossOrigin(origins = "*")
public class OrderShopController {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = OrderShop.class.getSimpleName();
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  public void JwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
    this.jwtTokenUtil = jwtTokenUtil;
  }

  private OrderShopService orderShopService;

  @Autowired
  public void OrderService(OrderShopService orderShopService) {
    this.orderShopService = orderShopService;
  }

  @GetMapping
  @RolesAllowed({ ERole.Names.ADMIN })
  public ResponseObject<List<OrderResponseDTO>> getAllOrders(
      @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = DEFAULT_SIZE) Integer size,
      @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
      @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
    return new ResponseObject<>(
        HttpStatus.OK, "", this.orderShopService.getAllOrderShops(pageable).toList());
  }

  @GetMapping("/userId/{userId}")
  @RolesAllowed({ ERole.Names.CUSTOMER, ERole.Names.CUSTOMER, ERole.Names.SELLER, ERole.Names.ADMIN })
  public ResponseObject<List<OrderResponseDTO>> getAllOrdersByUser(
      @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = DEFAULT_SIZE) Integer size,
      @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
      @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
      @PathVariable(name = "userId") Long userId, HttpServletRequest request) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
    // this.LOGGER.info(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(
        HttpStatus.OK, "", this.orderShopService.getAllOrderShopsByUser(loginKey, userId, pageable).toList());
  }

  @GetMapping("/shopId/{shopId}")
  @RolesAllowed({ ERole.Names.SELLER, ERole.Names.ADMIN })
  public ResponseObject<Page<OrderResponseDTO>> getAllOrdersByShop(
      @PathVariable(name = "shopId") Long shopId,
      @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
      @RequestParam(name = "limit", required = false, defaultValue = DEFAULT_SIZE) Integer size,
      @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
      @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
      HttpServletRequest request) {
    Sort sort = Sort.by(sortField);
    sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
    Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
    boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains(
        ERole.ROLE_ADMIN.toString());
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(
        HttpStatus.OK, "", this.orderShopService.getAllOrderShopsByShop(loginKey, shopId, pageable, isAdmin));
  }

  @GetMapping("/{id}")
  public ResponseObject<OrderResponseDTO> getOrderById(@PathVariable(name = "id") Long id, HttpServletRequest request) {
    // this.LOGGER.info(String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(
        HttpStatus.OK, "", this.orderShopService.getOrderShopByOrderId(loginKey, id));
  }

  @PutMapping("/{id}/status")
  @RolesAllowed({ ERole.Names.SELLER, ERole.Names.ADMIN })
  public ResponseObject<OrderResponseDTO> updateStatus(@PathVariable(name = "id") Long id,
      @RequestBody OrderUpdateStatusDTO updateStatusDTO,
      HttpServletRequest request) {
    String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
    return new ResponseObject<>(HttpStatus.OK, String.format(Utils.UPDATE_OBJECT_SUCCESSFULLY, branchName),
        this.orderShopService.updateStatusOrderShop(loginKey, id, updateStatusDTO));
  }
}
