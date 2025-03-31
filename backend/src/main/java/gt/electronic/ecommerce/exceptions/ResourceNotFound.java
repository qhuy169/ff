package gt.electronic.ecommerce.exceptions;

/**
 * @author quang huy
 * @created 20/10/2025 - 1:58 PM
 */
public class ResourceNotFound extends RuntimeException {
  public ResourceNotFound(String message) {
    super(message);
  }

  public ResourceNotFound(String message, Throwable cause) {
    super(message, cause);
  }
}
