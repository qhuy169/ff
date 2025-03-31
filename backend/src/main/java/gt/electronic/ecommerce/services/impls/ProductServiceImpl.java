package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.dto.request.ProductCreationDTO;
import gt.electronic.ecommerce.dto.response.ProductGalleryDTO;
import gt.electronic.ecommerce.dto.response.ProductResponseDTO;
import gt.electronic.ecommerce.entities.*;
import gt.electronic.ecommerce.entities.keys.ProductDescriptionKey;
import gt.electronic.ecommerce.exceptions.ResourceAlreadyExistsException;
import gt.electronic.ecommerce.exceptions.ResourceNotFound;
import gt.electronic.ecommerce.exceptions.ResourceNotFoundException;
import gt.electronic.ecommerce.exceptions.UserNotPermissionException;
import gt.electronic.ecommerce.mapper.ProductMapper;
import gt.electronic.ecommerce.models.clazzs.ProductRating;
import gt.electronic.ecommerce.models.enums.EImageType;
import gt.electronic.ecommerce.models.enums.EProductStatus;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.models.enums.ESortOption;
import gt.electronic.ecommerce.repositories.*;
import gt.electronic.ecommerce.services.*;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:03 AM
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String branchName = Product.class.getSimpleName();
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

    private DescriptionService descriptionService;

    @Autowired
    public void DescriptionService(DescriptionService descriptionService) {
        this.descriptionService = descriptionService;
    }

    private FeedbackService feedbackService;

    @Autowired
    public void FeedbackService(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    private ImageService imageService;

    @Autowired
    public void ImageService(ImageService imageService) {
        this.imageService = imageService;
    }

    private LocationService locationService;

    @Autowired
    public void LocationService(LocationService locationService) {
        this.locationService = locationService;
    }

    private ProductDescriptionService productDescriptionService;

    @Autowired
    public void ProductDescriptionService(ProductDescriptionService productDescriptionService) {
        this.productDescriptionService = productDescriptionService;
    }

    private ProductMapper productMapper;

    @Autowired
    public void ProductMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    private ProductRepository productRepo;

    @Autowired
    public void ProductRepository(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    private SaleService saleService;

    @Autowired
    public void SaleService(SaleService saleService) {
        this.saleService = saleService;
    }

    private ShopRepository shopRepo;

    @Autowired
    public void ShopRepository(ShopRepository shopRepo) {
        this.shopRepo = shopRepo;
    }

    private UserService userService;

    @Autowired
    public void UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Page<ProductGalleryDTO> getAllProduct(Pageable pageable) {
        this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT, branchName));
        Page<Product> products = this.productRepo.findAll(pageable);
        if (products.getContent().size() < 1) {
            throw new ResourceNotFound(String.format(Utils.OBJECT_NOT_FOUND, branchName));
        }
        return products.map(product -> this.productMapper.productToProductGalleryDTO(product));
    }

    @Override
    public Page<ProductGalleryDTO> getAllProductOnTradingByPage(Pageable pageable) {
        this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT_BY_FIELD,
                branchName,
                "Status",
                EProductStatus.PRODUCT_TRADING));
        Page<Product> products = this.productRepo.findAllByStatus(EProductStatus.PRODUCT_TRADING, pageable);
        if (products.getContent().size() < 1) {
            throw new ResourceNotFound(String.format(Utils.OBJECT_NOT_FOUND, branchName));
        }
        return products.map(product -> this.productMapper.productToProductGalleryDTO(product));
    }

    @Override
    public Page<ProductGalleryDTO> filterProduct(
            String keyword,
            String brandName,
            String categoryName,
            String shopName,
            String locationString,
            int sortOption,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable) {
        this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT_BY_FIELD, branchName, "Keyword", keyword));
        List<Brand> brandList = new ArrayList<>();
        List<Category> categoryList = new ArrayList<>();
        Shop shopFound = null;
        Location locationFound = null;
        if (brandName != null) {
            Brand brandFound;
            brandFound = this.brandRepo.findByName(brandName)
                    .orElseThrow(() -> new ResourceNotFound(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                            Brand.class.getSimpleName(),
                            "Name",
                            brandName)));
            brandList.add(brandFound);
        }

        if (categoryName != null) {
            Category categoryFound;
            categoryFound = this.categoryRepo.findByName(categoryName)
                    .orElseThrow(() -> new ResourceNotFound(String.format(
                            Utils.OBJECT_NOT_FOUND_BY_FIELD,
                            Category.class.getSimpleName(),
                            "Name",
                            brandName)));
            categoryList.add(categoryFound);
        }
        if (shopName != null) {
            shopFound = this.shopRepo.findByName(shopName)
                    .orElseThrow(() -> new ResourceNotFound(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                            Shop.class.getSimpleName(),
                            "Name",
                            shopName)));
        }
        if (locationString != null && locationString.length() > 0) {
            locationFound = this.locationService.getLocation(Utils.getLocationFromLocationString(locationString));
        }
        Page<Product> productPage = this.productRepo.filterProductToPage(keyword,
                categoryList,
                brandList,
                shopFound,
                locationFound,
                minPrice,
                maxPrice, pageable);
        if (productPage.getContent().size() < 1) {
            throw new ResourceNotFound(String.format(Utils.OBJECT_NOT_FOUND, branchName));
        }
        return productPage.map(product -> this.productMapper.productToProductGalleryDTO(product));
    }

    @Override
    public Page<ProductGalleryDTO> filterProductByKeyword(
            String keyword, String locationString, int sortOption, BigDecimal minPrice, BigDecimal maxPrice,
            Pageable pageable) {
        this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT_BY_FIELD, branchName, "Keyword", keyword));
        Location locationFound = null;
        if (locationString != null && locationString.length() > 0) {
            locationFound = this.locationService.getLocation(Utils.getLocationFromLocationString(locationString));
        }
        Page<Product> productPage = this.productRepo.filterProductToPage(keyword, null, null, null, locationFound,
                minPrice, maxPrice,
                pageable);
        if (productPage.getContent().size() < 1) {
            throw new ResourceNotFound(String.format(Utils.OBJECT_NOT_FOUND, branchName));
        }
        return productPage.map(product -> this.productMapper.productToProductGalleryDTO(product));
    }

    @Override
    public Page<ProductGalleryDTO> getAllProductCategoryIdAndBrandId(
            String keyword,
            List<Integer> brandIds,
            List<Integer> categoryIds,
            Long shopId,
            String locationString,
            int sortOption,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable) {
        this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT_BY_THREE_FIELD,
                branchName,
                Brand.class.getSimpleName() + "ID",
                brandIds.toString(),
                Category.class.getSimpleName() + "ID",
                categoryIds.toString(),
                "Option",
                ESortOption.values()[sortOption]));
        List<Brand> brandList = new ArrayList<>();
        List<Category> categoryList = new ArrayList<>();
        Shop shopFound = null;
        Location locationFound = null;
        if (brandIds.size() > 0) {
            Brand brandFound;
            for (Integer brandId : brandIds) {
                brandFound = this.brandRepo.findById(brandId)
                        .orElse(null);
                if (brandFound != null) {
                    brandList.add(brandFound);
                }
            }
        }

        if (categoryIds.size() > 0) {
            Category categoryFound;
            for (Integer categoryId : categoryIds) {
                categoryFound = this.categoryRepo.findById(categoryId)
                        .orElse(null);
                if (categoryFound != null) {
                    categoryList.add(categoryFound);
                }
            }
        }
        if (shopId != null) {
            shopFound = this.shopRepo.findById(shopId)
                    .orElseThrow(() -> new ResourceNotFound(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                            Shop.class.getSimpleName(),
                            "ID",
                            shopId)));
        }
        if (locationString != null && locationString.length() > 0) {
            locationFound = this.locationService.getLocation(Utils.getLocationFromLocationString(locationString));
        }
        // boolean hasBrand = false, hasCategory = false;
        if (brandList.size() < 1) {
            brandList = null;
        }
        if (categoryList.size() < 1) {
            categoryList = null;
        }
        Sort.Order order = pageable.getSort().getOrderFor("price");
        if (sortOption == ESortOption.POPULAR.ordinal() || sortOption == ESortOption.TOP_SALES.ordinal()
                || order != null) {
            List<Product> productList = this.productRepo.filterProductToList(keyword,
                    categoryList,
                    brandList,
                    shopFound,
                    locationFound,
                    minPrice,
                    maxPrice);
            if (productList.size() < 1) {
                throw new ResourceNotFound(String.format(Utils.OBJECT_NOT_FOUND, branchName));
            }
            if (sortOption == ESortOption.TOP_SALES.ordinal()) {
                productList.sort((o1, o2) -> {
                    long delta = getSoldQuantityById(o2.getId()) - getSoldQuantityById(o1.getId());
                    if (delta == 0) {
                        if (order != null) {
                            return comparePriceProduct(o1, o2, order);
                        }
                        return 0;
                    } else if (delta > 0) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
            } else if (sortOption == ESortOption.POPULAR.ordinal()) {
                productList.sort((o1, o2) -> {
                    ProductRating rating1 = feedbackService.getProductRatingByProduct(o1.getId());
                    ProductRating rating2 = feedbackService.getProductRatingByProduct(o2.getId());
                    double delta = rating1.getStar() * rating1.getTotalVote()
                            - rating2.getStar() * rating2.getTotalVote();
                    if (delta == 0) {
                        if (order != null) {
                            return comparePriceProduct(o1, o2, order);
                        }
                    }
                    return delta > 0 ? -1 : (delta == 0 ? 0 : 1);
                });
            } else {
                productList.sort((o1, o2) -> comparePriceProduct(o1, o2, order));
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), productList.size());
            Page<Product> productPage = new PageImpl<>(productList.subList(start, end), pageable, productList.size());
            return productPage.map(product -> this.productMapper.productToProductGalleryDTO(product));
        } else {
            Page<Product> productPage = this.productRepo.filterProductToPage(keyword,
                    categoryList,
                    brandList,
                    shopFound,
                    locationFound,
                    minPrice,
                    maxPrice, pageable);
            if (productPage.getContent().size() < 1) {
                throw new ResourceNotFound(String.format(Utils.OBJECT_NOT_FOUND, branchName));
            }
            return productPage.map(product -> this.productMapper.productToProductGalleryDTO(product));
        }
    }

    private int comparePriceProduct(Product o1, Product o2, Sort.Order order) {
        Sale sale1 = saleService.getMostOptimalSaleByProduct(o1.getId());
        Sale sale2 = saleService.getMostOptimalSaleByProduct(o2.getId());
        BigDecimal price1 = Utils.getPriceProduct(o1, sale1);
        BigDecimal price2 = Utils.getPriceProduct(o2, sale2);
        if (order.isAscending()) {
            return price1.subtract(price2).compareTo(new BigDecimal(0));
        } else {
            return price2.subtract(price1).compareTo(new BigDecimal(0));
        }
    }

    @Override
    public ProductResponseDTO getProductById(String loginKey, Long id, boolean haveSentiment) {
        this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT, branchName, "ID", id));
        Product product = this.productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFound(
                        String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, branchName, "ID", id)));
        if (haveSentiment && loginKey != null) {
            User authorFound = this.userService.getUserByLoginKey(loginKey);
            if ((authorFound.getShop() == null || !Objects.equals(authorFound.getShop().getId(), product.getShop()
                    .getId())) && authorFound.getRole() != ERole.ROLE_ADMIN) {
                haveSentiment = false;
            }
        }
        return this.productMapper.productToProductResponseDTO(product, haveSentiment);
    }

    @Override
    public ProductResponseDTO getProductBySlug(String slug, boolean haveSentiment) {
        this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT, branchName, "Slug", slug));
        Product product = this.productRepo.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFound(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                        branchName,
                        "Slug",
                        slug)));
        return this.productMapper.productToProductResponseDTO(product, haveSentiment);
    }

    @Override
    public ProductResponseDTO createProduct(
            String loginKey,
            ProductCreationDTO creationDTO,
            MultipartFile thumbnailFile,
            MultipartFile[] imageGalleryFile,
            boolean... isAdmin) {
        this.LOGGER.info(String.format(Utils.LOG_CREATE_OBJECT, branchName, "Name", creationDTO.getName()));
        // check product name is existed
        Optional<Product> productFound = this.productRepo.findByName(creationDTO.getName());
        if (productFound.isPresent()) {
            throw new ResourceAlreadyExistsException(String.format(Utils.OBJECT_EXISTED_BY_FIELD,
                    branchName,
                    "Name",
                    creationDTO.getName()));
        }

        // check brand input is valid
        Brand brandFound = this.brandRepo.findById(creationDTO.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                        Brand.class.getSimpleName(),
                        "ID",
                        creationDTO.getBrandId())));

        // check category input is valid
        Category categoryFound = this.categoryRepo.findById(creationDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                        Category.class.getSimpleName(),
                        "ID",
                        creationDTO.getCategoryId())));
        // check shop input is valid
        Shop shopFound;
        if (isAdmin.length > 0 && isAdmin[0]) {
            shopFound = this.shopRepo.findById(creationDTO.getShopId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                            Shop.class.getSimpleName(),
                            "ID",
                            creationDTO.getShopId())));
        } else {
            shopFound = this.userService.getUserByLoginKey(loginKey).getShop();
        }

        Product product = new Product();
        product.setName(creationDTO.getName());
        product.setSlug(Utils.vnToSlug(product.getName()) + "." + UUID.randomUUID().toString().replace("-", ""));
        product.setDescription(creationDTO.getDescription());
        product.setPrice(creationDTO.getPrice());
        product.setQuantity(creationDTO.getQuantity());
        product.setShop(shopFound);
        product.setStatus(creationDTO.getStatus());
        product.setBrand(brandFound);
        product.setCategory(categoryFound);

        // Set thumbnail
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            Image thumbnailImage = this.imageService.createImageByMultipartFile(thumbnailFile,
                    EImageType.IMAGE_PRODUCT);
            product.setThumbnail(thumbnailImage);
        }

        // Set images
        if (imageGalleryFile != null && imageGalleryFile.length > 0) {
            Set<Image> imageGallery = new HashSet<>();
            for (MultipartFile multipartFile : imageGalleryFile) {
                if (!multipartFile.isEmpty()) {
                    Image image = this.imageService.createImageByMultipartFile(multipartFile,
                            EImageType.IMAGE_PRODUCT_GALLERY);
                    imageGallery.add(image);
                }
            }
            product.setImageGallery(imageGallery);
        }

        Product saved = this.productRepo.save(product);

        // Set descriptions
        if (creationDTO.getDescriptions() != null) {
            // for (DescriptionCreationDTO descriptionDTO : creationDTO.getDescriptions()) {
            for (var entry : creationDTO.getDescriptions().entrySet()) {
                Description description = new Description();
                // description.setName(descriptionDTO.getName());
                description.setName(entry.getKey());
                description.addCategory(categoryFound);
                description = descriptionService.createDescription(description);
                ProductDescription productDescription = new ProductDescription();
                productDescription.setId(new ProductDescriptionKey(saved.getId(), description.getId()));
                productDescription.setProduct(saved);
                productDescription.setDescription(description);
                // productDescription.setValue(descriptionDTO.getValue());
                productDescription.setValue(entry.getValue());
                saved.addDescription(
                        this.productDescriptionService.createOrUpdateProductDescription(productDescription));
            }
        }

        return this.productMapper.productToProductResponseDTO(product);
    }

    @Override
    public ProductResponseDTO updateProduct(
            String loginKey,
            Long id,
            ProductCreationDTO creationDTO,
            MultipartFile thumbnailFile,
            MultipartFile[] imageGalleryFile,
            boolean... isAdmin) {
        this.LOGGER.info(String.format(Utils.LOG_UPDATE_OBJECT, branchName, "ID", id));
        // check product is existed
        Product entityFound = this.productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        Utils.OBJECT_NOT_FOUND_BY_FIELD,
                        branchName,
                        "ID",
                        id)));

        // check brand input is valid
        Brand brandFound = this.brandRepo.findById(creationDTO.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                        Brand.class.getSimpleName(),
                        "ID",
                        creationDTO.getBrandId())));

        // check category input is valid
        Category categoryFound = this.categoryRepo.findById(creationDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                        Category.class.getSimpleName(),
                        "ID",
                        creationDTO.getCategoryId())));

        // check shop input is valid
        Shop shopFound;
        if (isAdmin.length > 0 && isAdmin[0]) {
            shopFound = this.shopRepo.findById(creationDTO.getShopId())
                    .orElseThrow(() -> new ResourceNotFoundException(String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                            Shop.class.getSimpleName(),
                            "ID",
                            creationDTO.getShopId())));
        } else {
            shopFound = this.userService.getUserByLoginKey(loginKey).getShop();
        }

        if (!Objects.equals(entityFound.getShop().getId(), shopFound.getId())) {
            throw new UserNotPermissionException(Utils.USER_NOT_PERMISSION);
        }

        entityFound.setName(creationDTO.getName());
        entityFound.setSlug(
                Utils.vnToSlug(entityFound.getName()) + "." + UUID.randomUUID().toString().replace("-", ""));
        entityFound.setDescription(creationDTO.getDescription());
        entityFound.setPrice(creationDTO.getPrice());
        entityFound.setQuantity(creationDTO.getQuantity());
        entityFound.setShop(shopFound);
        entityFound.setStatus(creationDTO.getStatus());
        entityFound.setBrand(brandFound);
        entityFound.setCategory(categoryFound);

        // update thumbnail
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            // delete old thumbnail if it exists
            if (entityFound.getThumbnail() != null) {
                this.imageService.deleteImageById(entityFound.getThumbnail().getId());
            }
            // set new thumbnail
            Image thumbnailImage = this.imageService.createImageByMultipartFile(thumbnailFile,
                    EImageType.IMAGE_PRODUCT);
            entityFound.setThumbnail(thumbnailImage);
        }

        // update image gallery
        if (imageGalleryFile != null && imageGalleryFile.length > 0) {
            // delete old image gallery if it exists
            if (entityFound.getImageGallery() != null && entityFound.getImageGallery().size() > 0) {
                for (Image image : entityFound.getImageGallery()) {
                    this.imageService.deleteImageById(image.getId());
                }
            }
            // set new image gallery
            Set<Image> imageGallery = new HashSet<>();
            for (MultipartFile multipartFile : imageGalleryFile) {
                if (!multipartFile.isEmpty()) {
                    Image image = this.imageService.createImageByMultipartFile(multipartFile,
                            EImageType.IMAGE_PRODUCT_GALLERY);
                    imageGallery.add(image);
                }
            }
            entityFound.setImageGallery(imageGallery);
        }

        // update descriptions
        if (creationDTO.getDescriptions() != null) {
            // delete old descriptions if it existst
            if (entityFound.getDescriptions() != null) {
                entityFound.setDescriptions(new HashSet<>());
            }
            // set new descriptions
            Set<ProductDescription> descriptions = new HashSet<>();
            // for (DescriptionCreationDTO descriptionDTO : creationDTO.getDescriptions()) {
            for (var entry : creationDTO.getDescriptions().entrySet()) {
                Description description = new Description();
                // description.setName(descriptionDTO.getName());
                description.setName(entry.getKey());
                description.addCategory(categoryFound);
                description = descriptionService.createDescription(description);
                ProductDescription productDescription = new ProductDescription();
                productDescription.setId(new ProductDescriptionKey(entityFound.getId(), description.getId()));
                productDescription.setProduct(entityFound);
                productDescription.setDescription(description);
                // productDescription.setValue(descriptionDTO.getValue());
                productDescription.setValue(entry.getValue());
                descriptions.add(productDescription);
            }
            entityFound.setDescriptions(descriptions);
        }

        return this.productMapper.productToProductResponseDTO(this.productRepo.save(entityFound));
    }

    @Override
    public ProductResponseDTO deleteProductById(String loginKey, Long id, boolean... isAdmin) {
        this.LOGGER.info(String.format(Utils.LOG_DELETE_OBJECT, branchName, "ID", id));
        Product entityFound = this.productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        Utils.OBJECT_NOT_FOUND_BY_FIELD,
                        branchName,
                        "ID",
                        id)));
        // check shop input is valid
        Shop shopFound;
        if (isAdmin.length > 0 && !isAdmin[0]) {
            shopFound = this.userService.getUserByLoginKey(loginKey).getShop();
            if (!Objects.equals(entityFound.getShop().getId(), shopFound.getId())) {
                throw new UserNotPermissionException(Utils.USER_NOT_PERMISSION);
            }
        }
        // delete thumbnail
        Image thumbnail = entityFound.getThumbnail();
        if (thumbnail != null) {
            this.imageService.deleteImageById(thumbnail.getId());
        }

        // delete image gallery
        Set<Image> imageGallery = entityFound.getImageGallery();
        if (imageGallery != null && imageGallery.size() > 0) {
            for (Image image : imageGallery) {
                this.imageService.deleteImageById(image.getId());
            }
        }

        // delete Product
        this.productRepo.deleteById(id);
        return null;

    }

    @Override
    public Long getSoldQuantityById(Long id) {
        Product entityFound = this.productRepo.findById(id).orElse(null);
        if (entityFound != null) {
            Long soldQuantity = this.productRepo.getSoldQuantityByProduct(entityFound);
            if (soldQuantity != null) {
                return soldQuantity;
            }
        }
        return 0L;
    }

    @Override
    public void createDescription(Product product, Map<String, String> descriptions) {
        // Set descriptions
        if (descriptions != null) {
            // for (DescriptionCreationDTO descriptionDTO : creationDTO.getDescriptions()) {
            for (var entry : descriptions.entrySet()) {
                Description description = new Description();
                // description.setName(descriptionDTO.getName());
                description.setName(entry.getKey());
                description.addCategory(product.getCategory());
                description = descriptionService.createDescription(description);
                ProductDescription productDescription = new ProductDescription();
                productDescription.setId(new ProductDescriptionKey(product.getId(), description.getId()));
                productDescription.setProduct(product);
                productDescription.setDescription(description);
                // productDescription.setValue(descriptionDTO.getValue());
                productDescription.setValue(entry.getValue());
                product.addDescription(
                        this.productDescriptionService.createOrUpdateProductDescription(productDescription));
            }
        }
    }

}
