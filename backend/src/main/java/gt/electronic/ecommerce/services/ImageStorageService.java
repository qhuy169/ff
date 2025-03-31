package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.models.enums.EImageType;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author quang huy
 * @created 09/09/2025 - 7:17 PM
 */
public interface ImageStorageService {
  String storeFile(MultipartFile file, EImageType imageType);

  byte[] readFileContent(String fileName);

  void deleteFile(String path);
}