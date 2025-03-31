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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponseDTO {
  private Integer id;
  private String name;
  private String description;
  private String img;
}
