package gt.electronic.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author quang huy
 * @created 20/11/2025 - 8:22 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentUrlResponseDTO {
  String payUrl;
  Date createDate;
  Date expireDate;
}
