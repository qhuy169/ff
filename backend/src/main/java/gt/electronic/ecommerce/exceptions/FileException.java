package gt.electronic.ecommerce.exceptions;

/**
 * @author quang huy
 * @created 09/09/2025 - 7:20 PM
 * @project gt-backend
 */
public class FileException extends RuntimeException {
  public FileException() {
  }

  public FileException(String message) {
    super(message);
  }

  public FileException(String message, Throwable cause) {
    super(message, cause);
  }
}
