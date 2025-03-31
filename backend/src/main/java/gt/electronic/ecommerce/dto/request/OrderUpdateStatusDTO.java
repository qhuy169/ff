package gt.electronic.ecommerce.dto.request;

import gt.electronic.ecommerce.models.enums.EOrderStatus;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author quang huy
 * @created 21/11/2025 - 10:17 PM
 */
@Data
@ToString
public class OrderUpdateStatusDTO {
  private EOrderStatus status;
  private String log;
  private String shipOrderCode;
  private Date expectedDeliveryTime;
  private BigDecimal transportFee;
}
