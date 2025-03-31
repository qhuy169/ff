package gt.electronic.ecommerce.services;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import gt.electronic.ecommerce.dto.request.PaypalCreationDTO;
import gt.electronic.ecommerce.dto.request.PaypalForShopPriceCreationDTO;
import gt.electronic.ecommerce.models.enums.PaypalPaymentIntent;
import gt.electronic.ecommerce.models.enums.PaypalPaymentMethod;

public interface PaypalService {
    Payment createPayment(
            PaypalCreationDTO creationDTO,
            PaypalPaymentMethod method,
            PaypalPaymentIntent intent,
            String cancelUrl,
            String successUrl) throws PayPalRESTException;

    Payment createPaymentForShopPrice(
            PaypalForShopPriceCreationDTO creationDTO,
            PaypalPaymentMethod method,
            PaypalPaymentIntent intent,
            String cancelUrl,
            String successUrl) throws PayPalRESTException;

    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}
