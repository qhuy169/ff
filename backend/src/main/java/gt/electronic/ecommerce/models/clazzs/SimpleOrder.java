package gt.electronic.ecommerce.models.clazzs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author quang huy
 * @created 03/12/2025 - 4:25 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SimpleOrder {
  private Long orderId;
  private BigDecimal totalPriceProduct;
  private BigDecimal totalFee;
  private BigDecimal totalDiscount;
  private BigDecimal totalPrice;
}
