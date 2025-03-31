package gt.electronic.ecommerce.dto.request;

import gt.electronic.ecommerce.models.enums.EOrderStatus;
import gt.electronic.ecommerce.models.enums.EPaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author quang huy
 * @created 13/10/2025 - 4:38 PM
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderUpdatePaymentDTO {
  private EPaymentType payment;
  private EOrderStatus status;
  private Date expectedDeliveryTime;
  private BigDecimal transportFee;
}
