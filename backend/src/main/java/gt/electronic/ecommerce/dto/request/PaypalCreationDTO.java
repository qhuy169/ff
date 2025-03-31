package gt.electronic.ecommerce.dto.request;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class PaypalCreationDTO {
    Long orderId;
    String fullName;
    String redirectUrl;
    BigDecimal totalPrice;
}
