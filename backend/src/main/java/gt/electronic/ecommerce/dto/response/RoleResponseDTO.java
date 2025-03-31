package gt.electronic.ecommerce.dto.response;

import lombok.*;

/**
 * @author quang huy
 * @created 09/09/2025 - 7:05 PM
 * @project gt-backend
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {
  private Long id;
  private String name;
  private String description;
}
