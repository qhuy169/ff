package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.dto.request.CategoryCreationDTO;
import gt.electronic.ecommerce.dto.response.CategoryResponseDTO;
import gt.electronic.ecommerce.entities.Category;
import gt.electronic.ecommerce.entities.Image;
import gt.electronic.ecommerce.entities.Shop;
import gt.electronic.ecommerce.exceptions.ResourceNotFound;
import gt.electronic.ecommerce.exceptions.ResourceNotFoundException;
import gt.electronic.ecommerce.mapper.CategoryMapper;
import gt.electronic.ecommerce.models.enums.EImageType;
import gt.electronic.ecommerce.repositories.CategoryRepository;
import gt.electronic.ecommerce.repositories.ShopRepository;
import gt.electronic.ecommerce.services.CategoryService;
import gt.electronic.ecommerce.services.ImageService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:04 AM
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = Category.class.getSimpleName();
  private CategoryMapper categoryMapper;

  @Autowired
  public void CategoryMapper(CategoryMapper categoryMapper) {
    this.categoryMapper = categoryMapper;
  }

  private CategoryRepository categoryRepo;

  @Autowired
  public void CategoryRepository(CategoryRepository categoryRepo) {
    this.categoryRepo = categoryRepo;
  }

  private ImageService imageService;

  @Autowired
  public void ImageService(ImageService imageService) {
    this.imageService = imageService;
  }

  private ShopRepository shopRepo;

  @Autowired
  public void ShopRepository(ShopRepository shopRepo) {
    this.shopRepo = shopRepo;
  }

  @Override
  public List<CategoryResponseDTO> getAllCategories(String keyword) {
    this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT, branchName));
    List<Category> categoryPage = this.categoryRepo.findAll(keyword);
    return categoryPage.stream().map(Category -> this.categoryMapper.categoryToCategoryResponseDTO(Category)).collect(
        Collectors.toList());
  }

  @Override
  public List<CategoryResponseDTO> getAllCategoriesByShop(Long shopId) {
    this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT_BY_FIELD, branchName, "ShopId", shopId));
    Shop shopFound = this.shopRepo
        .findById(shopId)
        .orElseThrow(
            () -> new ResourceNotFound(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    branchName,
                    "ShopId",
                    shopId)));
    List<Category> entityList = this.categoryRepo.getALlCategoriesByShop(shopFound);
    return entityList.stream().map(category -> this.categoryMapper.categoryToCategoryResponseDTO(category)).collect(
        Collectors.toList());
  }

  @Override
  public CategoryResponseDTO getCategoryById(Integer id) {
    this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT, branchName, "ID", id));
    return this.categoryMapper.categoryToCategoryResponseDTO(
        this.categoryRepo
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFound(
                    String.format(
                        Utils.OBJECT_NOT_FOUND_BY_FIELD,
                        branchName,
                        "ID",
                        id))));
  }

  @Override
  public CategoryResponseDTO getCategoryBySlug(String slug) {
    this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT, branchName, "Slug", slug));
    Category category = this.categoryRepo
        .findBySlug(slug)
        .orElseThrow(
            () -> new ResourceNotFound(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    branchName,
                    "Slug",
                    slug)));
    return this.categoryMapper.categoryToCategoryResponseDTO(category);
  }

  @Override
  public CategoryResponseDTO createCategory(CategoryCreationDTO creationDTO, MultipartFile imageFile) {
    this.LOGGER.info(
        String.format(
            Utils.LOG_CREATE_OBJECT,
            branchName,
            "Name",
            creationDTO.getName()));

    Category category = new Category();
    category.setName(creationDTO.getName());
    category.setSlug(Utils.vnToSlug(category.getName()));
    category.setDescription(creationDTO.getDescription());
    if (creationDTO.getParentCategoryId() != null) {
      Category categoryParentFound = this.categoryRepo
          .findById(creationDTO.getParentCategoryId())
          .orElseThrow(
              () -> new ResourceNotFoundException(
                  String.format(
                      Utils.OBJECT_NOT_FOUND_BY_FIELD,
                      branchName,
                      "ID",
                      creationDTO.getParentCategoryId())));
      category.setParentCategory(categoryParentFound);
    }
    if (imageFile != null && !imageFile.isEmpty()) {
      Image image = this.imageService.createImageByMultipartFile(
          imageFile, EImageType.IMAGE_CATEGORY);
      category.setThumbnail(image);
    }

    return this.categoryMapper.categoryToCategoryResponseDTO(this.categoryRepo.save(category));
  }

  @Override
  public CategoryResponseDTO updateCategory(
      Integer id, CategoryCreationDTO creationDTO, MultipartFile imageFile) {
    this.LOGGER.info(
        String.format(Utils.LOG_UPDATE_OBJECT, branchName, "ID", id));
    Category categoryFound = this.categoryRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(
        String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, branchName, "ID", id)));

    categoryFound.setName(creationDTO.getName());
    categoryFound.setSlug(Utils.vnToSlug(categoryFound.getName()));
    categoryFound.setDescription(creationDTO.getDescription());
    if (creationDTO.getParentCategoryId() != null) {
      Category categoryParentFound = this.categoryRepo
          .findById(creationDTO.getParentCategoryId())
          .orElseThrow(
              () -> new ResourceNotFoundException(
                  String.format(
                      Utils.OBJECT_NOT_FOUND_BY_FIELD,
                      branchName,
                      "ID",
                      creationDTO.getParentCategoryId())));
      categoryFound.setParentCategory(categoryParentFound);
    }
    if (imageFile != null) {
      if (categoryFound.getThumbnail() != null) {
        this.imageService.deleteImageById(categoryFound.getThumbnail().getId());
      }
      if (!imageFile.isEmpty()) {
        Image image = this.imageService.createImageByMultipartFile(
            imageFile, EImageType.IMAGE_CATEGORY);
        categoryFound.setThumbnail(image);
      }
    }

    return this.categoryMapper.categoryToCategoryResponseDTO(this.categoryRepo.save(categoryFound));
  }

  @Override
  public CategoryResponseDTO deleteCategoryById(Integer id) {
    this.LOGGER.info(
        String.format(Utils.LOG_DELETE_OBJECT, branchName, "ID", id));
    Category CategoryFound = this.categoryRepo
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    branchName,
                    "ID",
                    id)));
    // delete Thumbnail
    Image image = CategoryFound.getThumbnail();
    if (image != null) {
      this.imageService.deleteImageById(image.getId());
    }

    // delete Category
    this.categoryRepo.deleteById(id);
    return null;
  }
}
