package gt.electronic.ecommerce.dto.request;

import gt.electronic.ecommerce.utils.Utils;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author quang huy
 * @created 01/11/2025 - 8:08 PM
 */
@Data
public class ShopCreationDTO {
  @NotNull(message = "An name is required!")
  private String name;

  private String description;

  private Long userId;

  @Size(message = "Invalid phone size.", max = 13, min = 9)
  // @NotNull(message="An phone is required!")
  @Pattern(regexp = (Utils.REGEX_PHONE), message = "Invalid phone")
  private String phone;

  @Size(message = "Invalid email size.", max = 320, min = 10)
  // @NotNull(message = "An email is required!")
  @Pattern(regexp = (Utils.REGEX_EMAIL), message = "Invalid email")
  private String email;

  private AddressCreationDTO address;
}
