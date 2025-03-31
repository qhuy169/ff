package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.dto.request.BrandCreationDTO;
import gt.electronic.ecommerce.dto.response.BrandResponseDTO;
import gt.electronic.ecommerce.entities.Brand;
import gt.electronic.ecommerce.entities.Category;
import gt.electronic.ecommerce.entities.Image;
import gt.electronic.ecommerce.entities.Shop;
import gt.electronic.ecommerce.exceptions.ResourceAlreadyExistsException;
import gt.electronic.ecommerce.exceptions.ResourceNotFound;
import gt.electronic.ecommerce.exceptions.ResourceNotFoundException;
import gt.electronic.ecommerce.mapper.BrandMapper;
import gt.electronic.ecommerce.models.enums.EImageType;
import gt.electronic.ecommerce.repositories.BrandRepository;
import gt.electronic.ecommerce.repositories.CategoryRepository;
import gt.electronic.ecommerce.repositories.ShopRepository;
import gt.electronic.ecommerce.services.BrandService;
import gt.electronic.ecommerce.services.ImageService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:03 AM
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = Brand.class.getSimpleName();
  private BrandMapper brandMapper;

  @Autowired
  public void BrandMapper(BrandMapper brandMapper) {
    this.brandMapper = brandMapper;
  }

  private BrandRepository brandRepo;

  @Autowired
  public void BrandRepository(BrandRepository brandRepo) {
    this.brandRepo = brandRepo;
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
  public List<BrandResponseDTO> getAllBrands(String keyword) {
    this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT, branchName));
    List<Brand> brandList = this.brandRepo.findAll(keyword);
    // if (brandList.getContent().size() < 1) {
    // throw new ResourceNotFoundException(String.format(Utils.OBJECT_NOT_FOUND,
    // branchName));
    // }
    return brandList.stream().map(brand -> this.brandMapper.brandToBrandResponseDTO(brand))
        .collect(Collectors.toList());
  }

  @Override
  public List<BrandResponseDTO> getAllBrandsByCategory(String keyword, Integer categoryId) {
    this.LOGGER.info(
        String.format(Utils.LOG_GET_ALL_OBJECT_BY_FIELD, branchName, Category.class.getSimpleName(),
            categoryId));
    Category categoryFound = this.categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException(
        String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, Category.class.getSimpleName(), "ID", categoryId)));
    List<Brand> brandList = this.brandRepo.findAllByCategory(categoryFound, keyword);
    // if (brandList.getContent().size() < 1) {
    // throw new ResourceNotFound(String.format(Utils.OBJECT_NOT_FOUND,
    // branchName));
    // }
    return brandList.stream().map(brand -> this.brandMapper.brandToBrandResponseDTO(brand))
        .collect(Collectors.toList());
  }

  @Override
  public List<BrandResponseDTO> getAllBrandsByShop(Long shopId) {
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
    List<Brand> entityList = this.brandRepo.getALlBrandsByShop(shopFound);
    return entityList.stream().map(entity -> this.brandMapper.brandToBrandResponseDTO(entity)).collect(
        Collectors.toList());
  }

  @Override
  public BrandResponseDTO getBrandById(Integer id) {
    this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT, branchName, "ID", id));
    Brand brand = this.brandRepo.findById(id).orElseThrow(
        () -> new ResourceNotFound(
            String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, branchName, "ID", id)));
    return this.brandMapper.brandToBrandResponseDTO(brand);
  }

  @Override
  public BrandResponseDTO getBrandBySlug(String slug) {
    this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT, branchName, "Slug", slug));
    Brand brand = this.brandRepo
        .findBySlug(slug)
        .orElseThrow(
            () -> new ResourceNotFoundException(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    branchName,
                    "Slug",
                    slug)));
    return this.brandMapper.brandToBrandResponseDTO(brand);
  }

  @Override
  public BrandResponseDTO createBrand(BrandCreationDTO creationDTO, MultipartFile imageFile) {
    this.LOGGER.info(
        String.format(Utils.LOG_CREATE_OBJECT, branchName, "Name", creationDTO.getName()));
    Brand brandFound = this.brandRepo.findByName(creationDTO.getName()).orElse(null);
    if (brandFound != null) {
      throw new ResourceAlreadyExistsException(
          String.format(Utils.OBJECT_EXISTED_BY_FIELD, branchName, "Name", creationDTO.getName()));
    }
    Brand brand = new Brand();
    brand.setName(creationDTO.getName());
    brand.setSlug(Utils.vnToSlug(brand.getName()));
    brand.setDescription(creationDTO.getDescription());
    if (imageFile != null && !imageFile.isEmpty()) {
      Image image = this.imageService.createImageByMultipartFile(imageFile, EImageType.IMAGE_BRAND);
      brand.setImage(image);
    }

    return this.brandMapper.brandToBrandResponseDTO(this.brandRepo.save(brand));
  }

  @Override
  public BrandResponseDTO updateBrand(Integer id, BrandCreationDTO creationDTO,
      MultipartFile imageFile) {
    this.LOGGER.info(String.format(Utils.LOG_UPDATE_OBJECT, branchName, "ID", id));
    Optional<Brand> brandFound = this.brandRepo.findById(id);
    if (brandFound.isPresent()) {
      Brand brand = brandFound.get();
      brand.setName(creationDTO.getName());
      brand.setSlug(Utils.vnToSlug(brand.getName()));
      brand.setDescription(creationDTO.getDescription());
      if (imageFile != null) {
        if (brand.getImage() != null) {
          this.imageService.deleteImageById(brand.getImage().getId());
        }
        if (!imageFile.isEmpty()) {
          Image image = this.imageService.createImageByMultipartFile(imageFile, EImageType.IMAGE_BRAND);
          brand.setImage(image);
        }
      }

      return this.brandMapper.brandToBrandResponseDTO(this.brandRepo.save(brand));
    }
    throw new ResourceNotFoundException(
        String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, branchName, "ID", id));
  }

  @Override
  public BrandResponseDTO deleteBrandById(Integer id) {
    this.LOGGER.info(String.format(Utils.LOG_DELETE_OBJECT, branchName, "ID", id));
    Brand BrandFound = this.brandRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(
        String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, branchName, "ID", id)));
    // delete image
    Image image = BrandFound.getImage();
    if (image != null) {
      this.imageService.deleteImageById(image.getId());
    }

    // delete Brand
    this.brandRepo.deleteById(id);
    return null;
  }
}
