package gt.electronic.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * @author quang huy
 * @created 07/03/2025 - 11:06 PM
 * @project gt-backend
 */
@Getter
@Setter
@NoArgsConstructor
// @AllArgsConstructor
public class ResponseObject<T> {
  private Object status;
  private String message;
  private T data;

  public ResponseObject(Object status, String message, @Nullable T data) {
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public ResponseObject(Object status, String message) {
    this.status = status;
    this.message = message;
    this.data = null;
  }
}
