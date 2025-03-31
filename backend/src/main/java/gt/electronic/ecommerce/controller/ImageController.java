package gt.electronic.ecommerce.controller;

import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.entities.Image;
import gt.electronic.ecommerce.services.ImageService;
import gt.electronic.ecommerce.services.ImageStorageService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static gt.electronic.ecommerce.utils.Utils.PRE_API_IMAGE;

/**
 * @author quang huy
 * @created 10/09/2025 - 10:02 PM
 * @project gt-backend
 */
@RestController
@RequestMapping(PRE_API_IMAGE)
@CrossOrigin(origins = "*")
public class ImageController {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = Image.class.getSimpleName();

  private ImageService imageService;

  @Autowired
  public void ImageService(ImageService imageService) {
    this.imageService = imageService;
  }

  private ImageStorageService imageStorageService;

  @Autowired
  public void ImageStorageService(ImageStorageService imageStorageService) {
    this.imageStorageService = imageStorageService;
  }

  @DeleteMapping("/{id}")
  public ResponseObject<Image> deleteImage(@PathVariable(name = "id") Long id) {
    return new ResponseObject<>(HttpStatus.OK, String.format(Utils.DELETE_OBJECT_SUCCESSFULLY, branchName),
        this.imageService.deleteImageById(id));
  }

  @GetMapping(value = "/{image_type}/{name}")
  public ResponseEntity<byte[]> getImageFileByPath(
      @PathVariable(name = "image_type") String image_type,
      @PathVariable(name = "name") String name) {
    try {
      String path = image_type + "/" + name;
      byte[] bytes = this.imageStorageService.readFileContent(path);
      return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(bytes);
    } catch (Exception e) {
      return ResponseEntity.noContent().build();
    }
  }

  @GetMapping(value = "/file/{id}")
  public ResponseEntity<byte[]> getImageFileByPath(@PathVariable(name = "id") Long id) {
    try {
      String path = this.imageService.getImageById(id).getPath();
      byte[] bytes = this.imageStorageService.readFileContent(path);
      return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(bytes);
    } catch (Exception e) {
      return ResponseEntity.noContent().build();
    }
  }
}
