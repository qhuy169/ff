package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.exceptions.FileException;
import gt.electronic.ecommerce.exceptions.ResourceNotFoundException;
import gt.electronic.ecommerce.models.enums.EImageType;
import gt.electronic.ecommerce.services.ImageStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author quang huy
 * @created 09/09/2025 - 7:17 PM
 */
@Service
@Slf4j
public class ImageStorageServiceImpl implements ImageStorageService {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  private final Path storageFolder = Paths.get("uploads");

  private Boolean isImageFile(MultipartFile file) {
    String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
    assert fileExtension != null;
    return Arrays.asList(new String[] { "png", "jpg", "jpeg", "bmp" })
        .contains(fileExtension.trim().toLowerCase());
  }

  @Override
  public String storeFile(MultipartFile file, EImageType imageType) {
    try {
      log.info("Checking file...");
      // Check file is empty?
      if (file.isEmpty()) {
        log.info("File input is null");
        throw new FileException("Failed to store empty file");
      }
      // Check file is image?
      if (!isImageFile(file)) {
        log.info("You can only upload image file like .png, .jpg, .jpeg, .bmp");
        throw new FileException("You can only upload image file");
      }
      // Check file size less than 5MB?
      float fileSize = file.getSize() / 1_000_000.0f;
      if (fileSize > 5.0f) {
        log.info("File is too large");
        throw new FileException("File size must be less than 5MB");
      }
      // File must be renamed before storage
      String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
      String generatedFileName = UUID.randomUUID().toString().replace("-", "") + "." + fileExtension;

      // Check uploadPath?
      Path destinationFolder = this.storageFolder.resolve(Paths.get(imageType.toString())).normalize().toAbsolutePath();
      if (!Files.exists(destinationFolder)) {
        log.info("Creating folder...");
        Files.createDirectories(destinationFolder);
      }
      String path = imageType + "/" + generatedFileName;
      Path destinationFilePath = this.storageFolder.resolve(Paths.get(path)).normalize().toAbsolutePath();
      // log.info(destinationFilePath.getParent().toString());
      // log.info(this.storageFolder.toAbsolutePath().toString());
      // if
      // (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath()))
      // {
      // log.info("Not found path to store file");
      // throw new FileException("Cannot store file outside current directory.");
      // }
      // Copy file to the destination file path
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
      } catch (Exception exception) {
        throw new FileException(exception.getMessage());
      }
      log.info("New image was saved, name: {}", generatedFileName);
      return path;
    } catch (Exception e) {
      throw new FileException("Failed to store empty file", e);
    }
  }

  @Override
  public byte[] readFileContent(String path) {
    try {
      Path file = storageFolder.resolve(path);

      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
        return bytes;
      } else {
        throw new ResourceNotFoundException("Could not read file: " + path);
      }
    } catch (IOException ioException) {
      throw new FileException("Could no read file: " + path, ioException);
    }
  }

  @Override
  public void deleteFile(String path) {
    try {
      Path storageFolder = this.storageFolder.resolve(Paths.get(path)).normalize().toAbsolutePath();
      FileSystemUtils.deleteRecursively(storageFolder.toFile());
    } catch (Exception exception) {
      throw new FileException(exception.getMessage());
    }
  }
}
