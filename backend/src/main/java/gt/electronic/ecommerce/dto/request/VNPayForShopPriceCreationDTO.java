package gt.electronic.ecommerce.dto.request;

import lombok.Data;

@Data
public class VNPayForShopPriceCreationDTO {
    Long shopId;
    String fullName;
    Long shopPriceId;
    String redirectUrl;
    String locate;
    String bankCode;
}
