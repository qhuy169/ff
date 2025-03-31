package gt.electronic.ecommerce.dto.response;

import lombok.Data;

import java.util.Date;

/**
 * @author quang huy
 * @created 03/11/2025 - 12:51 AM
 */
@Data
public class ShopSimpleResponseDTO {
  private Long id;
  private String name;
  private String slug;
  private String description;
  private UserSimpleResponseDTO user;
  private String email;
  private String phone;
  private AddressResponseDTO address;
  private String avatar;
  private String background;
  private String lastLogin;
}
