package gt.electronic.ecommerce.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * @author quang huy
 * @created 08/10/2025 - 8:48 PM
 * @project gt-backend
 */
@Getter
@Setter
public class ResourceNotValidException extends RuntimeException {
  public ResourceNotValidException(String message) {
    super(message);
  }

  public ResourceNotValidException(String message, Throwable cause) {
    super(message, cause);
  }
}
