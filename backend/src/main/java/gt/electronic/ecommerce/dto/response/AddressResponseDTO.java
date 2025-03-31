package gt.electronic.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author quang huy
 * @created 20/09/2025 - 9:29 PM
 * @project gt-backend
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO {
  // private String line;
  // private String commune;
  // private String district;
  // private String province;
  private String homeAdd;
  private String ward;
  private String district;
  private String city;
}
