package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.dto.request.RegisterShopPriceDTO;
import gt.electronic.ecommerce.dto.response.ShopResponseDTO;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.Optional;

public interface PaymentService {
    Optional<String> updatePaymentHistory(String paymentCode, Date payAt, boolean isSuccess);

    ShopResponseDTO registerShopPrice(RegisterShopPriceDTO registerShopPriceDTO);
}
