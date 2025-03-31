package gt.electronic.ecommerce.dto.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PaypalForShopPriceCreationDTO {
    Long shopId;
    String fullName;
    Long shopPriceId;
    String redirectUrl;
}
