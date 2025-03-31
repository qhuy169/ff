package gt.electronic.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import gt.electronic.ecommerce.models.enums.EGender;
import lombok.*;

import java.util.Date;

/**
 * @author quang huy
 * @created 09/09/2025 - 7:03 PM
 * @project gt-backend
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
  private Long id;
  private String username;
  // private boolean isChangedUsername;
  private String fullName;
  private String firstName;
  private String lastName;
  private String email;
  private boolean isEmailVerified;
  private String phone;
  private boolean isPhoneVerified;
  private String identityCard;
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date birthDate;
  private int gender;
  private AddressResponseDTO address;
  private Long shopId;
  private boolean enabled;
  private String avatar;
  // private ERole[] roles;
  private int role;
  private Date createdAt;
  private Date updatedAt;
  private Date lastLogin;
}
