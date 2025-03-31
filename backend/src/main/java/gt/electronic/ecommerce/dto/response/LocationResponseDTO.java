package gt.electronic.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author quang huy
 * @created 11/09/2025 - 4:56 PM
 * @project gt-backend
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponseDTO {
  private Long id;

  private String homeAdd;

  private String ward;

  private String district;

  private String city;
}
