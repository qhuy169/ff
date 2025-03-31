package gt.electronic.ecommerce.dto.request;

import gt.electronic.ecommerce.models.enums.EGender;
import gt.electronic.ecommerce.utils.Utils;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author quang huy
 * @created 08/09/2025 - 8:04 PM
 * @project gt-backend
 */
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
@Data
public class AuthLoginDTO {
  @Size(message = "Invalid phone size.", max = 13, min = 9)
  // @NotNull(message="An phone is required!")
  @Pattern(regexp = (Utils.REGEX_PHONE), message = "Invalid phone")
  private String phone;

  @Size(message = "Invalid email size.", max = 320, min = 10)
  // @NotNull(message = "An email is required!")
  @Pattern(regexp = (Utils.REGEX_EMAIL), message = "Invalid email")
  private String email;

  @Length(message = "Invalid password size.", min = 4, max = 32)
  // @NotNull( message="An password is required!")
  private String password;

  private boolean otp;

  public AuthLoginDTO(String lastName, String firstName, EGender gender, String phone, String email, String password) {
    this.phone = phone;
    this.email = email;
    this.password = password;
    this.otp = false;
  }
}
