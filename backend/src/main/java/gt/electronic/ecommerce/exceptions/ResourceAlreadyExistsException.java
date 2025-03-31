package gt.electronic.ecommerce.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 * @author quang huy
 * @created 09/09/2025 - 9:42 PM
 * @project gt-backend
 */
@Getter
@Setter
public class ResourceAlreadyExistsException extends RuntimeException {
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private String resourceName;
  private String fieldName;
  private Object fieldValue;

  public ResourceAlreadyExistsException() {
    super();
  }

  public ResourceAlreadyExistsException(String mess) {
    super(mess);
  }

  public ResourceAlreadyExistsException(String mess, Throwable cause) {
    super(mess, cause);
  }

  public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
    super(String.format("%s is existed with %s : '%s'", resourceName, fieldName, fieldValue));
    this.resourceName = resourceName;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
  }

  public ResourceAlreadyExistsException(String originResouceName, String firstFieldName, Object firstFieldValue,
      String secondFieldName, Object secondFieldValue) {
    super(String.format("%s is existed with %s : '%s' and %s : '%s'",
        originResouceName, firstFieldName, firstFieldValue, secondFieldName, secondFieldValue));
    this.resourceName = originResouceName;
    this.fieldName = String.format("[{%s}, {%s}]", firstFieldName, secondFieldName);
    this.fieldValue = String.format("[{%s}, {%s}]", firstFieldValue, secondFieldValue);
    ;
  }
}
