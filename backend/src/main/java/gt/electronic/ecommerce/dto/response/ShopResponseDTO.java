package gt.electronic.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import gt.electronic.ecommerce.entities.ShopPrice;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

/**
 * @author quang huy
 * @created 01/11/2025 - 8:17 PM
 */
@Data
public class ShopResponseDTO {
  private Long id;
  private String name;
  private String slug;
  private String description;
  private UserSimpleResponseDTO user;
  private String email;
  private boolean isEmailVerified;
  private String phone;
  private boolean isPhoneVerified;
  private double percent;
  private AddressResponseDTO address;
  private boolean enabled;
  private String avatar;
  private String background;
  private Date createdAt;
  private Date updatedAt;
  private String lastLogin;
  private ShopPriceResponseDTO shopPrice;
  private Date registerPriceAt;
  private Date endPriceAt;
}
