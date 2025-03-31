package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.dto.request.SaleCreationDTO;
import gt.electronic.ecommerce.dto.response.SaleResponseDTO;
import gt.electronic.ecommerce.entities.Image;
import gt.electronic.ecommerce.entities.Product;
import gt.electronic.ecommerce.entities.Sale;
import gt.electronic.ecommerce.entities.User;
import gt.electronic.ecommerce.exceptions.ResourceNotFoundException;
import gt.electronic.ecommerce.mapper.SaleMapper;
import gt.electronic.ecommerce.models.enums.EImageType;
import gt.electronic.ecommerce.repositories.ProductRepository;
import gt.electronic.ecommerce.repositories.SaleRepository;
import gt.electronic.ecommerce.repositories.UserRepository;
import gt.electronic.ecommerce.services.ImageService;
import gt.electronic.ecommerce.services.SaleService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author quang huy
 * @created 17/09/2025 - 1:46 PM
 */
@Service
@Transactional
public class SaleServiceImpl implements SaleService {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = Sale.class.getSimpleName();
  private ImageService imageService;

  @Autowired
  public void ImageService(ImageService imageService) {
    this.imageService = imageService;
  }

  private SaleMapper saleMapper;

  @Autowired
  public void SaleMapper(SaleMapper saleMapper) {
    this.saleMapper = saleMapper;
  }

  private ProductRepository productRepo;

  @Autowired
  public void ProductRepository(ProductRepository productRepo) {
    this.productRepo = productRepo;
  }

  private SaleRepository saleRepo;

  @Autowired
  public void SaleRepository(SaleRepository saleRepo) {
    this.saleRepo = saleRepo;
  }

  private UserRepository userRepo;

  @Autowired
  public void UserRepository(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public Page<SaleResponseDTO> getAllSales(boolean isHasChild, Pageable pageable) {
    this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT, branchName));
    Page<Sale> sales = this.saleRepo.findAll(pageable);
    if (sales.getContent().size() < 1) {
      throw new ResourceNotFoundException(
          String.format(Utils.OBJECT_NOT_FOUND, branchName));
    }

    return sales.map(sale -> this.saleMapper.saleToSaleResponseDTO(sale, isHasChild));
  }

  @Override
  public Page<SaleResponseDTO> searchAllSales(
      String name,
      Double fromPercent,
      Double toPercent,
      Date fromDate,
      Date toDate,
      boolean isHasChild,
      Pageable pageable) {
    // this.LOGGER.info(String.format(Utils.LOG_GET_ALL_OBJECT, branchName);
    Page<Sale> sales = this.saleRepo.search(name, fromPercent, toPercent, fromDate, toDate, pageable);
    if (sales.getContent().size() < 1) {
      throw new ResourceNotFoundException(
          String.format(Utils.OBJECT_NOT_FOUND, branchName));
    }

    return sales.map(sale -> this.saleMapper.saleToSaleResponseDTO(sale, isHasChild));
  }

  @Override
  public SaleResponseDTO getSaleById(Long id, boolean isHasChild) {
    this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT, branchName, "ID", id));
    Sale saleFound = this.saleRepo
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    branchName,
                    "ID",
                    id)));
    return this.saleMapper.saleToSaleResponseDTO(saleFound);
  }

  @Override
  public Sale getMostOptimalSaleByProduct(Long productId) {
    Product productFound = this.productRepo
        .findById(productId)
        .orElse(null);
    if (productFound == null) {
      return null;
    }
    List<Sale> saleList = this.saleRepo.findMostOptimalSaleByProduct(productFound);
    return saleList.size() > 0 ? saleList.get(0) : null;
  }

  @Override
  public SaleResponseDTO createSale(
      SaleCreationDTO creationDTO, MultipartFile thumbnailFile) {
    this.LOGGER.info(
        String.format(
            Utils.LOG_CREATE_OBJECT, branchName, "Name", creationDTO.getName()));
    Sale sale = new Sale();
    sale.setName(creationDTO.getName());
    sale.setDescription(creationDTO.getDescription());
    sale.setPercent(creationDTO.getPercent());

    // set product gallery
    if (creationDTO.getProductIds() != null && creationDTO.getProductIds().size() > 0) {
      Set<Product> products = new HashSet<>();
      for (Long productId : creationDTO.getProductIds()) {
        Product productFound = this.productRepo
            .findById(productId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format(
                        Utils.OBJECT_NOT_FOUND_BY_FIELD,
                        Product.class.getSimpleName(),
                        "ID",
                        productId)));
        products.add(productFound);
      }
      sale.setProducts(products);
    }

    // set creator
    if (creationDTO.getCreatorId() != null) {
      User userFound = this.userRepo
          .findById(creationDTO.getCreatorId())
          .orElseThrow(
              () -> new ResourceNotFoundException(
                  String.format(
                      Utils.OBJECT_NOT_FOUND_BY_FIELD, branchName, "ID", creationDTO.getCreatorId())));
      sale.setCreator(userFound);
    }

    sale.setStartDate(creationDTO.getStartDate());
    sale.setEndDate(creationDTO.getEndDate());

    // Set thumbnail
    if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
      Image thumbnailImage = this.imageService.createImageByMultipartFile(thumbnailFile, EImageType.IMAGE_SALE);
      sale.setThumbnail(thumbnailImage);
    }

    return this.saleMapper.saleToSaleResponseDTO(this.saleRepo.save(sale), true);
  }

  @Override
  public SaleResponseDTO updateSale(
      Long id, SaleCreationDTO creationDTO, MultipartFile thumbnailFile) {
    this.LOGGER.info(
        String.format(
            Utils.LOG_CREATE_OBJECT, branchName, "Name", creationDTO.getName()));
    Sale saleFound = this.saleRepo
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    branchName,
                    "ID",
                    id)));
    saleFound.setName(creationDTO.getName());
    saleFound.setDescription(creationDTO.getDescription());
    saleFound.setPercent(creationDTO.getPercent());

    // update product gallery
    if (creationDTO.getProductIds() != null && creationDTO.getProductIds().size() > 0) {
      Set<Product> products = new HashSet<>();
      for (Long productId : creationDTO.getProductIds()) {
        Product productFound = this.productRepo
            .findById(productId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format(
                        Utils.OBJECT_NOT_FOUND_BY_FIELD,
                        Product.class.getSimpleName(),
                        "ID",
                        productId)));
        products.add(productFound);
      }
      saleFound.setProducts(products);
    }

    // update creator
    if (creationDTO.getCreatorId() != null) {
      User userFound = this.userRepo
          .findById(creationDTO.getCreatorId())
          .orElseThrow(
              () -> new ResourceNotFoundException(
                  String.format(
                      Utils.OBJECT_NOT_FOUND_BY_FIELD, branchName, "ID", creationDTO.getCreatorId())));
      saleFound.setCreator(userFound);
    }

    if (creationDTO.getStartDate() != null) {
      saleFound.setStartDate(creationDTO.getStartDate());
    }

    if (creationDTO.getEndDate() != null) {
      saleFound.setEndDate(creationDTO.getEndDate());
    }

    // update thumbnail
    if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
      // delete old thumbnail if it exists
      if (saleFound.getThumbnail() != null) {
        this.imageService.deleteImageById(saleFound.getThumbnail().getId());
      }
      // set new thumbnail
      Image thumbnailImage = this.imageService.createImageByMultipartFile(thumbnailFile, EImageType.IMAGE_SALE);
      saleFound.setThumbnail(thumbnailImage);
    }

    return this.saleMapper.saleToSaleResponseDTO(this.saleRepo.save(saleFound), true);
  }

  @Override
  public SaleResponseDTO deleteSaleById(Long id) {
    this.LOGGER.info(String.format(Utils.LOG_DELETE_OBJECT, branchName, "ID", id));
    Sale saleFound = this.saleRepo
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    branchName,
                    "ID",
                    id)));
    // delete thumbnail
    Image thumbnail = saleFound.getThumbnail();
    if (thumbnail != null) {
      this.imageService.deleteImageById(thumbnail.getId());
    }

    // delete Sale
    this.saleRepo.deleteById(id);
    return null;
  }
}
