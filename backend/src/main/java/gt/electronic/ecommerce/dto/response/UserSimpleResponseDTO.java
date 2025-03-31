package gt.electronic.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author quang huy
 * @created 12/09/2025 - 8:09 PM
 * @project gt-backend
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleResponseDTO {
  private Long id;
  private String fullName;
  private String username;
  private String email;
  private String phone;
  private String avatar;
  private boolean isAdmin;
}
