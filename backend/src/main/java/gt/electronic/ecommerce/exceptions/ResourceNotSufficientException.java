package gt.electronic.ecommerce.exceptions;

/**
 * @author quang huy
 * @created 09/10/2025 - 8:39 PM
 * @project gt-backend
 */
public class ResourceNotSufficientException extends RuntimeException {
  public ResourceNotSufficientException(String message) {
    super(message);
  }

  public ResourceNotSufficientException(String message, Throwable cause) {
    super(message, cause);
  }
}
