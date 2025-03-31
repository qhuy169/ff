package gt.electronic.ecommerce.utils;

import gt.electronic.ecommerce.exceptions.FileException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @created 09/09/2025 - 10:49 PM
 */
public class FileUploadUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

  public static String saveFile(String uploadDir,
      MultipartFile multipartFile) {
    try {
      LOGGER.info("Checking file...");
      // Check file is empty?
      if (multipartFile.isEmpty()) {
        LOGGER.info("File input is null");
        throw new FileException("Failed to store empty file");
      }

      // Check file is image?
      if (!isImageFile(multipartFile)) {
        LOGGER.info("You can only upload image file like .png, .jpg, .jpeg, .bmp");
        throw new FileException("You can only upload image file");
      }

      // Check file size less than 5MB?
      float fileSize = multipartFile.getSize() / 1_000_000.0f;
      if (fileSize > 5.0f) {
        LOGGER.info("File is too large");
        throw new FileException("File size must be less than 5MB");
      }

      Path uploadPath = Paths.get(uploadDir);

      // Check uploadPath?
      if (!Files.exists(uploadPath)) {
        uploadPath = Files.createDirectories(uploadPath);
      }

      // File must be renamed before storage
      String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
      String generatedFileName = UUID.randomUUID().toString().replace("-", "");
      generatedFileName = generatedFileName + "." + fileExtension;
      Path destinationFilePath = uploadPath.resolve(Paths.get(generatedFileName)).normalize().toAbsolutePath();
      if (!destinationFilePath.getParent().equals(uploadPath.toAbsolutePath())) {
        LOGGER.info("Not found path to store file");
        throw new FileException("Cannot store file outside current directory.");
      }

      // Copy file to the destination file path
      try (InputStream inputStream = multipartFile.getInputStream()) {
        Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
      } catch (Exception exception) {
        throw new FileException(exception.getMessage());
      }
      LOGGER.info("New image was saved, name: {}", generatedFileName);
      return generatedFileName;
    } catch (Exception e) {
      throw new FileException("Failed to store empty file", e);
    }
  }

  private static Boolean isImageFile(MultipartFile file) {
    String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
    assert fileExtension != null;
    return Arrays.asList(new String[] { "png", "jpg", "jpeg", "bmp" })
        .contains(fileExtension.trim().toLowerCase());
  }

  public static void cleanDir(String dir) {
    Path dirPath = Paths.get(dir);

    try {
      Files.list(dirPath).forEach(file -> {
        if (!Files.isDirectory(file)) {
          try {
            Files.delete(file);
          } catch (IOException ex) {
            LOGGER.error("Could not delete file: " + file);
          }
        }
      });
    } catch (IOException ex) {
      LOGGER.error("Could not list directory: " + dirPath);
    }
  }

  public static void removeDir(String dir) {
    cleanDir(dir);

    try {
      Files.delete(Paths.get(dir));
    } catch (IOException e) {
      LOGGER.error("Could not remove directory: " + dir);
    }

  }
}
