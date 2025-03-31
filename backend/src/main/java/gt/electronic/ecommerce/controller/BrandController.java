package gt.electronic.ecommerce.controller;

import gt.electronic.ecommerce.dto.request.BrandCreationDTO;
import gt.electronic.ecommerce.dto.response.BrandResponseDTO;
import gt.electronic.ecommerce.dto.response.CategoryResponseDTO;
import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.entities.Brand;
import gt.electronic.ecommerce.services.BrandService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:53 AM
 * @project gt-backend
 */
@RestController
@RequestMapping(value = "/api/v1/brands")
@CrossOrigin(origins = "*")
public class BrandController {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = Brand.class.getSimpleName();
  private BrandService brandService;

  @Autowired
  public void BrandService(BrandService brandService) {
    this.brandService = brandService;
  }

  @GetMapping
  public ResponseObject<List<BrandResponseDTO>> getAllBrands(
      @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword) {
    return new ResponseObject<>(
        HttpStatus.OK, "", this.brandService.getAllBrands(keyword));
  }

  @GetMapping("/categoryId/{categoryId}")
  public ResponseObject<List<BrandResponseDTO>> getAllBrandsByCategory(
      @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
      @PathVariable(name = "categoryId") Integer categoryId) {
    return new ResponseObject<>(
        HttpStatus.OK, "", this.brandService.getAllBrandsByCategory(keyword,
            categoryId));
  }

  @GetMapping("/shopId/{shopId}")
  public ResponseObject<List<BrandResponseDTO>> getAllBrandsByShop(
      @PathVariable(name = "shopId") Long shopId) {
    return new ResponseObject<>(
        HttpStatus.OK, "", this.brandService.getAllBrandsByShop(shopId));
  }

  @GetMapping("/{id}")
  public ResponseObject<BrandResponseDTO> getBrandById(@PathVariable(name = "id") Integer id) {
    return new ResponseObject<>(HttpStatus.OK, "", this.brandService.getBrandById(id));
  }

  @GetMapping("/slug/{slug}")
  public ResponseObject<BrandResponseDTO> getBrandBySlug(@PathVariable(name = "slug") String slug) {
    return new ResponseObject<>(HttpStatus.OK, "", this.brandService.getBrandBySlug(slug));
  }

  @PostMapping
  public ResponseObject<BrandResponseDTO> createBrand(@RequestPart("data") @Valid BrandCreationDTO BrandCreationDTO,
      @RequestPart(value = "image", required = false) MultipartFile imageFile) {
    return new ResponseObject<>(HttpStatus.CREATED, String.format(Utils.CREATE_OBJECT_SUCCESSFULLY, branchName),
        this.brandService.createBrand(BrandCreationDTO, imageFile));
  }

  @PutMapping("/{id}")
  public ResponseObject<BrandResponseDTO> updateBrand(@PathVariable(name = "id") Integer id,
      @RequestPart("data") BrandCreationDTO BrandCreationDTO,
      @RequestPart(value = "image", required = false) MultipartFile imageFile) {
    return new ResponseObject<>(HttpStatus.OK, String.format(Utils.UPDATE_OBJECT_SUCCESSFULLY, branchName),
        this.brandService.updateBrand(id, BrandCreationDTO, imageFile));
  }

  @DeleteMapping("/{id}")
  public ResponseObject<BrandResponseDTO> deleteBrand(@PathVariable(name = "id") Integer id) {
    return new ResponseObject<>(HttpStatus.OK, String.format(Utils.DELETE_OBJECT_SUCCESSFULLY, branchName),
        this.brandService.deleteBrandById(id));
  }
}
