package gt.electronic.ecommerce.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.models.enums.EGender;
import gt.electronic.ecommerce.utils.Utils;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author quang huy
 * @created 09/09/2025 - 10:28 PM
 * @project gt-backend
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {

  @NotNull(message = "An username is required!")
  private String username;
  //
  // private boolean isChangedUsername;

  // Minimum eight characters, at least one uppercase letter, one lowercase
  // letter, one number and one special character
  @Length(message = "Invalid password size.", min = 4, max = 32)
  @NotNull(message = "An password is required!")
  // @Pattern(
  // regexp = (Utils.REGEX_PASSWORD),
  // message = "Invalid password")
  private String password;

  @NotNull(message = "An firstname is required!")
  private String firstName;

  @NotNull(message = "An lastname is required!")
  private String lastName;

  @Size(message = "Invalid email size.", max = 320, min = 10)
  // @NotNull(message = "An email is required!")
  @Pattern(regexp = (Utils.REGEX_EMAIL), message = "Invalid email")
  private String email;

  // Minimum eight characters, at least one uppercase letter, one lowercase
  // letter, one number and one special character
  @Size(message = "Invalid phone size.", max = 13, min = 9)
  // @NotNull(message = "An phone is required!")
  @Pattern(regexp = (Utils.REGEX_PHONE), message = "Invalid phone")
  private String phone;

  private EGender gender;

  private AddressCreationDTO address;

  private boolean enabled;

  private ERole role;
}
