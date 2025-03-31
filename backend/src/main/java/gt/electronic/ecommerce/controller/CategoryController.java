package gt.electronic.ecommerce.controller;

import gt.electronic.ecommerce.dto.request.CategoryCreationDTO;
import gt.electronic.ecommerce.dto.response.CategoryResponseDTO;
import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.entities.Category;
import gt.electronic.ecommerce.services.CategoryService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.util.List;

import static gt.electronic.ecommerce.utils.Utils.DEFAULT_PAGE;
import static gt.electronic.ecommerce.utils.Utils.DEFAULT_SIZE;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:53 AM
 * @project gt-backend
 */
@RestController
@RequestMapping(value = "/api/v1/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = Category.class.getSimpleName();
  private CategoryService categoryService;

  @Autowired
  public void categoryService(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @GetMapping
  public ResponseObject<List<CategoryResponseDTO>> getAllCategories(
      @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword) {
    return new ResponseObject<>(
        HttpStatus.OK, "", this.categoryService.getAllCategories(keyword));
  }

  @GetMapping("/shopId/{shopId}")
  public ResponseObject<List<CategoryResponseDTO>> getAllCategoriesByShop(
      @PathVariable(name = "shopId") Long shopId) {
    return new ResponseObject<>(
        HttpStatus.OK, "", this.categoryService.getAllCategoriesByShop(shopId));
  }

  @GetMapping("/{id}")
  public ResponseObject<CategoryResponseDTO> getCategoryById(@PathVariable(name = "id") Integer id) {
    return new ResponseObject<>(
        HttpStatus.OK, "", this.categoryService.getCategoryById(id));
  }

  @GetMapping("/slug/{slug}")
  public ResponseObject<CategoryResponseDTO> getCategoryBySlug(@PathVariable(name = "slug") String slug) {
    return new ResponseObject<>(
        HttpStatus.OK, "", this.categoryService.getCategoryBySlug(slug));
  }

  @PostMapping
  public ResponseObject<CategoryResponseDTO> createCategory(
      @RequestPart("data") @Valid CategoryCreationDTO categoryCreationDTO,
      @RequestPart(value = "image", required = false) MultipartFile imageFile) {
    return new ResponseObject<>(HttpStatus.CREATED, String.format(Utils.CREATE_OBJECT_SUCCESSFULLY, branchName),
        this.categoryService.createCategory(categoryCreationDTO, imageFile));
  }

  @PutMapping("/{id}")
  public ResponseObject<CategoryResponseDTO> updateCategory(
      @PathVariable(name = "id") Integer id,
      @RequestPart("data") @Valid CategoryCreationDTO categoryCreationDTO,
      @RequestPart(value = "image", required = false) MultipartFile imageFile) {
    return new ResponseObject<>(HttpStatus.OK, String.format(Utils.UPDATE_OBJECT_SUCCESSFULLY, branchName),
        this.categoryService.updateCategory(id, categoryCreationDTO, imageFile));
  }

  @DeleteMapping("/{id}")
  public ResponseObject<CategoryResponseDTO> deleteCategory(@PathVariable(name = "id") Integer id) {
    return new ResponseObject<>(HttpStatus.OK, String.format(Utils.DELETE_OBJECT_SUCCESSFULLY, branchName),
        this.categoryService.deleteCategoryById(id));
  }
}
