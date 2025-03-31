package gt.electronic.ecommerce.dto.request;

import gt.electronic.ecommerce.models.enums.EGender;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.utils.Utils;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author quang huy
 * @created 25/11/2025 - 4:24 PM
 */
@Data
@ToString
public class AuthRegisterDTO {
  // @NotNull(message = "An firstname is required!")
  private String firstName;

  // @NotNull(message = "An lastname is required!")
  private String lastName;

  private EGender gender;
  @Size(message = "Invalid phone size.", max = 13, min = 9)
  @NotNull(message = "An phone is required!")
  @Pattern(regexp = (Utils.REGEX_PHONE), message = "Invalid phone")
  private String phone;

  @Size(message = "Invalid email size.", max = 320, min = 10)
  @NotNull(message = "An email is required!")
  @Pattern(regexp = (Utils.REGEX_EMAIL), message = "Invalid email")
  private String email;

  @Length(message = "Invalid password size.", min = 4, max = 32)
  @NotNull(message = "An password is required!")
  private String password;

  private boolean otp;

  private ERole role = ERole.ROLE_CUSTOMER;

  private AddressCreationDTO address;

  public AuthRegisterDTO(
      String lastName,
      String firstName,
      EGender gender,
      String phone,
      String email,
      String password) {
    this.lastName = lastName;
    this.firstName = firstName;
    if (gender != null) {
      this.gender = gender;
    } else {
      this.gender = EGender.UNKNOWN;
    }
    this.phone = phone;
    this.email = email;
    this.password = password;
    this.otp = false;
  }

  public AuthRegisterDTO(AuthLoginDTO loginDTO) {
    this.lastName = "Vô";
    this.firstName = "Danh";
    this.gender = EGender.UNKNOWN;
    this.phone = loginDTO.getPhone();
    this.email = loginDTO.getEmail();
    this.password = loginDTO.getPassword();
    this.otp = loginDTO.isOtp();
  }
}
