package gt.electronic.ecommerce.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * @author quang huy
 * @created 09/09/2025 - 10:13 PM
 * @project gt-backend
 */
@Getter
@Setter
public class InvalidFieldException extends RuntimeException {
  private String resource;
  private String fieldName;
  private String action;

  public InvalidFieldException() {
    super();
  }

  public InvalidFieldException(String message) {
    super(message);
  }

  public InvalidFieldException(String resource, String fieldName, String action) {
    super(String.format("field %s in %s has an inappropriate value for action %s", fieldName, resource, action));
    this.resource = resource;
    this.fieldName = fieldName;
    this.action = action;
  }
}
