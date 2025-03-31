package gt.electronic.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:17 AM
 * @project gt-backend
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {
  private Integer id;
  private String title;
  private String href;
  private String description;
  private Integer parentCategoryId;
  private String img;
  private String icon;
}
