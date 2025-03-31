package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.entities.Image;
import gt.electronic.ecommerce.models.enums.EImageType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author quang huy
 * @created 10/09/2025 - 12:11 PM
 */
public interface ImageService {
  List<Image> getAllImage();

  Image getImageById(Long id);

  Image createImage(Image Image);

  Image createImageByMultipartFile(MultipartFile multipartFile, EImageType imageType);

  Image updateImage(Long id, Image Image);

  Image deleteImageById(Long id);
}
