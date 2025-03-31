package gt.electronic.ecommerce.controller;

import gt.electronic.ecommerce.dto.request.ProductCreationDTO;
import gt.electronic.ecommerce.dto.response.ProductGalleryDTO;
import gt.electronic.ecommerce.dto.response.ProductResponseDTO;
import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.entities.Product;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.models.enums.ESortOption;
import gt.electronic.ecommerce.services.ProductService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static gt.electronic.ecommerce.utils.Utils.DEFAULT_PAGE;
import static gt.electronic.ecommerce.utils.Utils.PRODUCT_PER_PAGE;

/**
 * @author quang huy
 * @created 11/09/2025 - 3:45 PM
 * @project gt-backend
 */
@RestController
@RequestMapping(value = "/api/v1/products")
@CrossOrigin(origins = "*")
public class ProductController {
        private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
        public static final String branchName = Product.class.getSimpleName();
        private JwtTokenUtil jwtTokenUtil;

        @Autowired
        public void JwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
                this.jwtTokenUtil = jwtTokenUtil;
        }

        private ProductService productService;

        @Autowired
        public void ProductService(ProductService productService) {
                this.productService = productService;
        }

        @GetMapping("")
        public ResponseObject<Page<ProductGalleryDTO>> getAllProduct(
                        @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
                        @RequestParam(name = "limit", required = false, defaultValue = PRODUCT_PER_PAGE) Integer size,
                        @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                        @RequestParam(name = "brandIds", required = false, defaultValue = "") List<Integer> brandIds,
                        @RequestParam(name = "categoryIds", required = false, defaultValue = "") List<Integer> categoryIds,
                        @RequestParam(name = "shopId", required = false, defaultValue = "") Long shopId,
                        @RequestParam(name = "sortField", required = false, defaultValue = "") String sortField,
                        @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
                        @RequestParam(name = "location", required = false, defaultValue = "") String locationString,
                        @RequestParam(name = "sortOption", required = false, defaultValue = "0") int sortOption,
                        @RequestParam(name = "minPrice", required = false, defaultValue = "0") BigDecimal minPrice,
                        @RequestParam(name = "maxPrice", required = false, defaultValue = "100000000") BigDecimal maxPrice) {
                List<Sort.Order> orderList = new ArrayList<>();
                if (!Objects.equals(sortField.trim(), "") || (Objects.equals(sortField.trim(),
                                "id") &&
                                sortOption == ESortOption.LATEST.ordinal())) {
                        orderList.add(sortDir.equals("asc") ? Sort.Order.asc(sortField) : Sort.Order.desc(sortField));
                }
                if (sortOption == ESortOption.LATEST.ordinal()) {
                        orderList.add(Sort.Order.desc("createdAt"));
                }
                Sort sort = Sort.by(orderList);
                Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
                return new ResponseObject<>(
                                HttpStatus.OK, "",
                                this.productService.getAllProductCategoryIdAndBrandId(keyword, brandIds,
                                                categoryIds,
                                                shopId,
                                                locationString,
                                                sortOption,
                                                minPrice,
                                                maxPrice,
                                                pageable));// .toList();
        }

        @GetMapping("/filter")
        public ResponseObject<Page<ProductGalleryDTO>> getAllProduct(
                        @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
                        @RequestParam(name = "limit", required = false, defaultValue = PRODUCT_PER_PAGE) Integer size,
                        @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
                        @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
                        @RequestParam(name = "brand", required = false, defaultValue = "") String brandName,
                        @RequestParam(name = "category", required = false, defaultValue = "") String categoryName,
                        @RequestParam(name = "shop", required = false, defaultValue = "") String shopName,
                        @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                        @RequestParam(name = "location", required = false, defaultValue = "") String locationString,
                        @RequestParam(name = "sortOption", required = false, defaultValue = "0") int sortOption,
                        @RequestParam(name = "minPrice", required = false, defaultValue = "0") BigDecimal minPrice,
                        @RequestParam(name = "maxPrice", required = false, defaultValue = "100000000") BigDecimal maxPrice) {
                Sort sort = Sort.by(sortField);
                sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
                Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
                return new ResponseObject<>(
                                HttpStatus.OK, "", this.productService.filterProduct(
                                                keyword, brandName, categoryName, shopName, locationString, sortOption,
                                                minPrice, maxPrice, pageable));
        }

        @GetMapping("/search")
        public ResponseObject<Page<ProductGalleryDTO>> getAllProductByKeyword(
                        @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
                        @RequestParam(name = "limit", required = false, defaultValue = PRODUCT_PER_PAGE) Integer size,
                        @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
                        @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir,
                        @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                        @RequestParam(name = "location", required = false, defaultValue = "") String locationString,
                        @RequestParam(name = "sortOption", required = false, defaultValue = "0") int sortOption,
                        @RequestParam(name = "minPrice", required = false, defaultValue = "0") BigDecimal minPrice,
                        @RequestParam(name = "maxPrice", required = false, defaultValue = "100000000") BigDecimal maxPrice) {
                if (sortField.equals("price")) {
                        sortField = "list_price";
                }
                Sort sort = Sort.by(sortField);
                sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
                Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
                return new ResponseObject<>(
                                HttpStatus.OK, "",
                                this.productService.filterProductByKeyword(keyword, locationString, sortOption,
                                                minPrice, maxPrice,
                                                pageable));
        }

        @GetMapping("/trading")
        public ResponseObject<Page<ProductGalleryDTO>> getAllProductOnTradingByPage(
                        @RequestParam(name = "page", required = false, defaultValue = DEFAULT_PAGE) Integer page,
                        @RequestParam(name = "limit", required = false, defaultValue = PRODUCT_PER_PAGE) Integer size,
                        @RequestParam(name = "sortField", required = false, defaultValue = "id") String sortField,
                        @RequestParam(name = "sortDir", required = false, defaultValue = "asc") String sortDir) {
                Sort sort = Sort.by(sortField);
                sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
                Pageable pageable = PageRequest.of(page > 0 ? page - 1 : 0, size, sort);
                return new ResponseObject<>(
                                HttpStatus.OK, "", this.productService.getAllProductOnTradingByPage(pageable));
        }

        @GetMapping("/{id}")
        public ResponseObject<ProductResponseDTO> getProductById(@PathVariable(name = "id") Long id,
                        @RequestParam(name = "haveSentiment", required = false, defaultValue = "false") boolean haveSentiment,
                        HttpServletRequest request) {

                String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
                return new ResponseObject<>(
                                HttpStatus.OK, "", this.productService.getProductById(loginKey, id, haveSentiment));
        }

        @GetMapping("/slug/{slug}")
        public ResponseObject<ProductResponseDTO> getProductBySlug(@PathVariable(name = "slug") String slug,
                        @RequestParam(name = "haveSentiment", required = false, defaultValue = "false") boolean haveSentiment) {
                return new ResponseObject<>(
                                HttpStatus.OK, "", this.productService.getProductBySlug(slug, haveSentiment));
        }

        @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
        @RolesAllowed({ ERole.Names.SELLER, ERole.Names.ADMIN })
        public ResponseObject<ProductResponseDTO> createProduct(
                        @RequestPart("data") @Valid ProductCreationDTO creationDTO,
                        @RequestPart(value = "image", required = false) MultipartFile imageFile,
                        @RequestPart(value = "images", required = false) MultipartFile[] imageFiles,
                        @RequestParam(name = "admin", required = false, defaultValue = "false") boolean isAdmin,
                        HttpServletRequest request) {
                isAdmin = isAdmin ? isAdmin
                                : SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString()
                                                .contains(
                                                                ERole.ROLE_ADMIN.toString());
                String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
                return new ResponseObject<>(HttpStatus.CREATED,
                                String.format(Utils.CREATE_OBJECT_SUCCESSFULLY, branchName),
                                this.productService.createProduct(loginKey,
                                                creationDTO,
                                                imageFile,
                                                imageFiles,
                                                isAdmin));
        }

        @PutMapping("/{id}")
        @RolesAllowed({ ERole.Names.SELLER, ERole.Names.ADMIN })
        public ResponseObject<ProductResponseDTO> updateProduct(
                        @PathVariable(name = "id") Long id,
                        @RequestPart("data") @Valid ProductCreationDTO creationDTO,
                        @RequestPart(value = "image", required = false) MultipartFile imageFile,
                        @RequestPart(value = "images", required = false) MultipartFile[] imageFiles,
                        @RequestParam(name = "admin", required = false, defaultValue = "false") boolean isAdmin,
                        HttpServletRequest request) {
                isAdmin = isAdmin ? isAdmin
                                : SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString()
                                                .contains(
                                                                ERole.ROLE_ADMIN.toString());
                String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
                return new ResponseObject<>(HttpStatus.OK, String.format(Utils.UPDATE_OBJECT_SUCCESSFULLY, branchName),
                                this.productService.updateProduct(loginKey,
                                                id,
                                                creationDTO,
                                                imageFile,
                                                imageFiles,
                                                isAdmin));
        }

        @DeleteMapping(value = "/{id}")
        public ResponseObject<ProductResponseDTO> deleteProductById(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "admin", required = false, defaultValue = "false") boolean isAdmin,
            HttpServletRequest request) {
            isAdmin = isAdmin ? isAdmin : SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString()
                            .contains(ERole.ROLE_ADMIN.toString());
            String loginKey = jwtTokenUtil.getUserNameFromRequest(request);
            return new ResponseObject<>(HttpStatus.OK, String.format(Utils.DELETE_OBJECT_SUCCESSFULLY, branchName),
                this.productService.deleteProductById(loginKey, id, isAdmin));
        }
        
}
