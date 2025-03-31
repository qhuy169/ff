package gt.electronic.ecommerce.dto.request;

import gt.electronic.ecommerce.entities.Shop;
import gt.electronic.ecommerce.models.enums.EShippingMethod;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author quang huy
 * @created 03/12/2025 - 8:50 PM
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderShopCreationDTO {
  private Long shopId;
  private EShippingMethod shippingMethod;
  private Date expectedDeliveryTime;
  private BigDecimal totalFee;
  @NotNull(message = "OrderItems not null!")
  private List<OrderDetailCreationDTO> items;

  public OrderShopCreationDTO(Shop shop, List<OrderDetailCreationDTO> items) {
    if (shop != null) {
      this.shopId = shop.getId();
      this.shippingMethod = EShippingMethod.GHN_EXPRESS;
      this.expectedDeliveryTime = new Date();
      this.totalFee = new BigDecimal(0);
      this.items = items;
    }
  }
}
