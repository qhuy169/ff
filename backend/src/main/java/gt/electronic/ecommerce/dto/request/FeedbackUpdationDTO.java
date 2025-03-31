package gt.electronic.ecommerce.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author quang huy
 * @created 13/09/2025 - 12:03 PM
 * @project gt-backend
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackUpdationDTO {
  private String content;
  private int star;
}
