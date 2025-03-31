package gt.electronic.ecommerce.controller;

import gt.electronic.ecommerce.dto.request.RegisterShopPriceDTO;
import gt.electronic.ecommerce.dto.request.ShopCreationDTO;
import gt.electronic.ecommerce.dto.response.*;
import gt.electronic.ecommerce.entities.Message;
import gt.electronic.ecommerce.entities.Shop;
import gt.electronic.ecommerce.models.clazzs.GroupOrderByDate;
import gt.electronic.ecommerce.models.clazzs.ShopOverview;
import gt.electronic.ecommerce.models.clazzs.ShopSentiment;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.models.enums.ETimeDistance;
import gt.electronic.ecommerce.services.*;
import gt.electronic.ecommerce.utils.JwtTokenUtil;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static gt.electronic.ecommerce.utils.Utils.*;

/**
 * @author quang huy
 * @created 03/11/2025 - 11:23 PM
 */
@RestController
@RequestMapping("/api/v1/shops")
@CrossOrigin(origins = "*")
public class ShopController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String branchName = Shop.class.getSimpleName();
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public void JwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    private MessageService messageService;

    @Autowired
    public void MessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    private PaymentService paymentService;

    @Autowired
    public void PaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    private ShopService shopService;

    @Autowired
    public void ShopService(ShopService shopService) {
        this.shopService = shopService;
    }

    private StatisticService statisticService;

    @Autowired
    public void StatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    private UtilsService utilsService;

    @Autowired
    public void UtilsService(UtilsService utilsService) {
        this.utilsService = utilsService;
    }

    @GetMapping
    public ResponseObject<List<ShopResponseDTO>> getAllShops(
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
            @RequestParam(name = "limit", required = false, defaultValue = DEFAULT_SIZE) Integer size,
            @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
        return new ResponseObject<>(
                HttpStatus.OK, "", this.shopService.getAllShops(keyword));
    }

    @GetMapping("/{id}")
    public ResponseObject<ShopResponseDTO> getShopById(@PathVariable(name = "id") Long id) {
        return new ResponseObject<>(HttpStatus.OK, "", this.shopService.getShopById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseObject<ShopResponseDTO> getShopBySlug(@PathVariable(name = "slug") String slug) {
        return new ResponseObject<>(HttpStatus.OK, "", this.shopService.getShopBySlug(slug));
    }

    @GetMapping("/userId/{userId}")
    public ResponseObject<ShopResponseDTO> getShopByUser(@PathVariable(name = "userId") Long userId) {
        return new ResponseObject<>(HttpStatus.OK, "", this.shopService.getShopByUser(userId));
    }

    @GetMapping("/access-token")
    @RolesAllowed({ ERole.Names.CUSTOMER, ERole.Names.SELLER, ERole.Names.ADMIN })
    public ResponseObject<ShopResponseDTO> getShopByAccessToken(HttpServletRequest request) {
        String accessToken = jwtTokenUtil.getAccessToken(request);
        return new ResponseObject<>(HttpStatus.OK, "", this.shopService.getShopByAccessToken(accessToken));
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    @RolesAllowed({ ERole.Names.CUSTOMER, ERole.Names.ADMIN })
    public ResponseObject<ShopResponseDTO> createShop(
            @RequestPart("data") @Valid ShopCreationDTO shopCreationDTO,
            @RequestParam(name = "isAdmin", required = false, defaultValue = "false") boolean isAdmin,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile,
            @RequestPart(value = "background", required = false) MultipartFile backgroundFile,
            HttpServletRequest request) {
        isAdmin = isAdmin ? isAdmin
                : SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains(
                        ERole.ROLE_ADMIN.toString());
        String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
        return new ResponseObject<>(HttpStatus.CREATED, String.format(Utils.CREATE_OBJECT_SUCCESSFULLY, branchName),
                this.shopService.createShop(loginKey,
                        shopCreationDTO,
                        avatarFile,
                        backgroundFile,
                        isAdmin));
    }

    @PutMapping("/{id}")
    @RolesAllowed({ ERole.Names.SELLER, ERole.Names.ADMIN })
    public ResponseObject<ShopResponseDTO> updateShop(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "isAdmin", required = false, defaultValue = "false") boolean isAdmin,
            @RequestPart("data") ShopCreationDTO shopCreationDTO,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile,
            @RequestPart(value = "background", required = false) MultipartFile backgroundFile,
            HttpServletRequest request) {
        isAdmin = isAdmin ? isAdmin
                : SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains(
                        ERole.ROLE_ADMIN.toString());
        String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
        return new ResponseObject<>(HttpStatus.OK, String.format(Utils.UPDATE_OBJECT_SUCCESSFULLY, branchName),
                this.shopService.updateShop(loginKey,
                        id,
                        shopCreationDTO,
                        avatarFile,
                        backgroundFile,
                        isAdmin));
    }

    @DeleteMapping("/{id}")
    @RolesAllowed({ ERole.Names.SELLER, ERole.Names.ADMIN })
    public ResponseObject<ShopResponseDTO> deleteShop(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "isAdmin", required = false, defaultValue = "false") boolean isAdmin,
            HttpServletRequest request) {
        String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
        return new ResponseObject<>(HttpStatus.OK, String.format(Utils.DELETE_OBJECT_SUCCESSFULLY, branchName),
                this.shopService.deleteShopById(loginKey, id, isAdmin));
    }

    @GetMapping("/overview/{shopId}")
    public ResponseObject<ShopOverview> getOverviewShopById(@PathVariable(name = "shopId") Long id) {
        return new ResponseObject<>(HttpStatus.OK, "", this.statisticService.getOverviewByShop(id));
    }

    @GetMapping("/statistic/order/{shopId}")
    @RolesAllowed({ ERole.Names.SELLER, ERole.Names.ADMIN })
    public ResponseObject<ShopStatisticResponseDTO> statisticOrder(
            @PathVariable(name = "shopId") Long shopId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(value = "timeDistance", required = false, defaultValue = "DAY") ETimeDistance timeDistance,
            HttpServletRequest request) {
        String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
        return new ResponseObject<>(HttpStatus.OK,
                "",
                this.statisticService.statisticOrderByShop(loginKey,
                        shopId,
                        startDate,
                        endDate,
                        timeDistance));
    }

    @GetMapping("/statistic/sentiment/{shopId}")
    @RolesAllowed({ ERole.Names.SELLER, ERole.Names.ADMIN })
    ResponseObject<ShopSentiment> statisticSentiment(
            @PathVariable(name = "shopId") Long shopId,
            HttpServletRequest request) {
        String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
        return new ResponseObject<>(HttpStatus.OK,
                "",
                this.statisticService.statisticSentimentByShop(loginKey, shopId));
    }

    @GetMapping("/statistic/sentiment/{shopId}/report-negative-product")
    @RolesAllowed({ ERole.Names.SELLER, ERole.Names.ADMIN })
    ResponseObject<ShopSentiment> reportNegativeProduct(
            @PathVariable(name = "shopId") Long shopId,
            HttpServletRequest request) {
        String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
        return new ResponseObject<>(HttpStatus.OK,
                "",
                this.statisticService.reportNegativeProductByShop(loginKey, shopId));
    }

    @GetMapping("/messages/{shopId}")
    @RolesAllowed({ ERole.Names.SELLER, ERole.Names.ADMIN })
    ResponseObject<Page<Message>> getMessage(
            @PathVariable(name = "shopId") Long shopId,
            @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
            @RequestParam(name = "limit", required = false, defaultValue = PRODUCT_PER_PAGE) Integer size,
            @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
            HttpServletRequest request) {
        String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
        return new ResponseObject<>(HttpStatus.OK,
                "",
                this.messageService.getMessageProduct(loginKey, shopId, pageable));
    }

    @GetMapping("/prices")
    // @RolesAllowed({ERole.Names.CUSTOMER, ERole.Names.SELLER, ERole.Names.ADMIN})
    ResponseObject<List<ShopPriceResponseDTO>> getAllShopPrice() {
        return new ResponseObject<>(HttpStatus.OK, "", this.utilsService.getAllShopPrice());
    }

    @PostMapping("/prices/register")
    // @RolesAllowed({ERole.Names.CUSTOMER, ERole.Names.SELLER, ERole.Names.ADMIN})
    ResponseObject<?> registerShopPrice(
            @RequestBody RegisterShopPriceDTO registerShopPriceDTO) {
        return new ResponseObject<>(
                HttpStatus.OK,
                "Successfully register Shop Price!",
                this.paymentService.registerShopPrice(registerShopPriceDTO));
    }
}
