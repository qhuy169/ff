package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.entities.Image;
import gt.electronic.ecommerce.exceptions.ResourceAlreadyExistsException;
import gt.electronic.ecommerce.exceptions.ResourceNotFoundException;
import gt.electronic.ecommerce.models.enums.EImageType;
import gt.electronic.ecommerce.repositories.ImageRepository;
import gt.electronic.ecommerce.services.ImageService;
import gt.electronic.ecommerce.services.ImageStorageService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

/**
 * @author quang huy
 * @created 10/09/2025 - 12:13 PM
 */
@Service
@Transactional
public class ImageServiceImpl implements ImageService {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = Image.class.getSimpleName();
  @Autowired
  private ImageRepository imageRepo;

  @Autowired
  private ImageService imageService;

  @Autowired
  private ImageStorageService storageService;

  @Override
  public List<Image> getAllImage() {
    return null;
  }

  @Override
  public Image getImageById(Long id) {
    this.LOGGER.info(String.format(Utils.LOG_GET_OBJECT, branchName, "ID", id));
    Image imageFound = this.imageRepo
        .findById(id)
        .orElseThrow(
            () -> new ResourceAlreadyExistsException(
                String.format(
                    Utils.OBJECT_NOT_FOUND_BY_FIELD,
                    branchName,
                    "ID",
                    id)));
    return imageFound;
  }

  @Override
  public Image createImage(Image Image) {
    return null;
  }

  @Override
  public Image createImageByMultipartFile(MultipartFile multipartFile, EImageType imageType) {
    if (multipartFile != null && !multipartFile.isEmpty()) {
      String pathFile = this.storageService.storeFile(multipartFile, imageType);
      Image image = new Image();
      image.setPath(pathFile);
      image.setImageType(imageType);
      Image savedImage = this.imageRepo.save(image);
      return savedImage;
    }
    return null;
  }

  @Override
  public Image updateImage(Long id, Image Image) {
    return null;
  }

  @Override
  public Image deleteImageById(Long id) {
    LOGGER.info(String.format(Utils.LOG_DELETE_OBJECT, branchName, "ID", id));
    Image imageFound = this.imageRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException(
        String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD, branchName, "ID", id)));
    // delete Image entity
    this.imageRepo.deleteById(id);

    if (!imageFound.getPath().startsWith("http")) {
      // delete Image file
      this.storageService.deleteFile(imageFound.getPath());
    }
    return null;
  }
}
