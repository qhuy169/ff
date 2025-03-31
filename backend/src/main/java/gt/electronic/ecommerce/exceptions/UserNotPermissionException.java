package gt.electronic.ecommerce.exceptions;

import gt.electronic.ecommerce.utils.Utils;

/**
 * @author quang huy
 * @created 13/10/2025 - 4:57 PM
 */
public class UserNotPermissionException extends RuntimeException {
  public UserNotPermissionException() {
    super(Utils.USER_NOT_PERMISSION);
  }

  public UserNotPermissionException(String message) {
    super(message);
  }

  public UserNotPermissionException(String message, Throwable cause) {
    super(message, cause);
  }
}
