package gt.electronic.ecommerce.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author quang huy
 * @created 20/11/2025 - 7:03 PM
 */
@Data
public class VNPayCreationDTO {
  Long orderId;
  String fullName;
  String redirectUrl;
  BigDecimal totalPrice;
  String locate;
  String bankCode;
}
