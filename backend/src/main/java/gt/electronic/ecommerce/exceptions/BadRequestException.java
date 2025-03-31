package gt.electronic.ecommerce.exceptions;

/**
 * @author quang huy
 * @created 27/11/2025 - 8:44 PM
 */
public class BadRequestException extends RuntimeException {
  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}