package gt.electronic.ecommerce.services.impls;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import gt.electronic.ecommerce.dto.request.PaypalCreationDTO;
import gt.electronic.ecommerce.dto.request.PaypalForShopPriceCreationDTO;
import gt.electronic.ecommerce.entities.Order;
import gt.electronic.ecommerce.entities.PaymentHistory;
import gt.electronic.ecommerce.entities.Shop;
import gt.electronic.ecommerce.entities.ShopPrice;
import gt.electronic.ecommerce.exceptions.ResourceNotFoundException;
import gt.electronic.ecommerce.models.enums.EPaymentCategory;
import gt.electronic.ecommerce.models.enums.EPaymentType;
import gt.electronic.ecommerce.models.enums.PaypalPaymentIntent;
import gt.electronic.ecommerce.models.enums.PaypalPaymentMethod;
import gt.electronic.ecommerce.repositories.OrderRepository;
import gt.electronic.ecommerce.repositories.PaymentHistoryRepository;
import gt.electronic.ecommerce.repositories.ShopPriceRepository;
import gt.electronic.ecommerce.repositories.ShopRepository;
import gt.electronic.ecommerce.services.PaypalService;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static gt.electronic.ecommerce.utils.Utils.OBJECT_NOT_FOUND_BY_FIELD;

@Service
@Transactional
public class PaypalServiceImpl implements PaypalService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String branchName = EPaymentType.PAYPAL.name();
    private APIContext apiContext;

    @Autowired
    public void APIContext(APIContext apiContext) {
        this.apiContext = apiContext;
    }

    private OrderRepository orderRepo;

    @Autowired
    public void OrderRepository(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    private PaymentHistoryRepository paymentHistoryRepo;

    @Autowired
    public void PaymentHistoryRepository(PaymentHistoryRepository paymentHistoryRepo) {
        this.paymentHistoryRepo = paymentHistoryRepo;
    }

    private ShopRepository shopRepo;

    @Autowired
    public void ShopRepository(ShopRepository shopRepo) {
        this.shopRepo = shopRepo;
    }

    private ShopPriceRepository shopPriceRepo;

    @Autowired
    public void ShopPriceRepository(ShopPriceRepository shopPriceRepo) {
        this.shopPriceRepo = shopPriceRepo;
    }

    @Override
    public Payment createPayment(PaypalCreationDTO creationDTO,
                                 PaypalPaymentMethod method,
                                 PaypalPaymentIntent intent,
                                 String cancelUrl,
                                 String successUrl) throws PayPalRESTException {
        this.LOGGER.info(String.format(Utils.LOG_CREATE_OBJECT_BY_TWO_FIELD,
                                       branchName,
                                       "orderId",
                                       creationDTO.getOrderId(),
                                       "totalPrice",
                                       creationDTO.getTotalPrice()));
        Amount amount = new Amount();
        amount.setCurrency("USD");
        BigDecimal currencyRate = new BigDecimal("0.00004");
        double total =
                creationDTO.getTotalPrice().multiply(currencyRate).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        String orderInfo = String.format(Utils.PAYMENT_ORDER,
                                         creationDTO.getFullName(),
                                         creationDTO.getOrderId(),
                                         creationDTO.getTotalPrice(),
                                         branchName);
        transaction.setDescription(orderInfo);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        payment = payment.create(apiContext);

        Order entityFound =
                this.orderRepo
                        .findById(creationDTO.getOrderId())
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                              Order.class.getSimpleName(),
                                                              "Id",
                                                              creationDTO.getOrderId())));
        entityFound.setPaymentOrderCode(payment.getId());
        this.orderRepo.save(entityFound);

        PaymentHistory paymentHistory =
                new PaymentHistory(orderInfo,
                                   creationDTO.getFullName(),
                                   creationDTO.getOrderId(),
                                   creationDTO.getTotalPrice(),
                                   EPaymentCategory.ORDER,
                                   EPaymentType.PAYPAL,
                                   payment.getId());
        paymentHistory.setRedirectUrl(creationDTO.getRedirectUrl());
        this.paymentHistoryRepo.save(paymentHistory);

        return payment;
    }

    @Override
    public Payment createPaymentForShopPrice(PaypalForShopPriceCreationDTO creationDTO,
                                             PaypalPaymentMethod method,
                                             PaypalPaymentIntent intent,
                                             String cancelUrl,
                                             String successUrl) throws PayPalRESTException {
        Optional<ShopPrice> shopPrice = this.shopPriceRepo.findById(creationDTO.getShopPriceId());
        if (shopPrice.isEmpty()) {
            throw new ResourceNotFoundException(String.format(OBJECT_NOT_FOUND_BY_FIELD,
                                                              ShopPrice.class.getSimpleName(),
                                                              "Id",
                                                              creationDTO.getShopPriceId()));
        }
        BigDecimal totalPrice = shopPrice.get().getPrice();
        this.LOGGER.info(String.format(Utils.LOG_CREATE_OBJECT_BY_TWO_FIELD,
                                       branchName,
                                       "shopId",
                                       creationDTO.getShopId(),
                                       "totalPrice",
                                       totalPrice));
        Amount amount = new Amount();
        amount.setCurrency("USD");
        BigDecimal currencyRate = new BigDecimal("0.00004");
        double total =
                totalPrice.multiply(currencyRate).setScale(2, RoundingMode.HALF_UP).doubleValue();
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        String orderInfo = String.format(Utils.PAYMENT_ORDER,
                                         creationDTO.getFullName(),
                                         "ShopPrice with ShopId = " + creationDTO.getShopId(),
                                         totalPrice,
                                         branchName);
        transaction.setDescription(orderInfo);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        payment = payment.create(apiContext);

        this.shopRepo
                .findById(creationDTO.getShopId())
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                                      Shop.class.getSimpleName(),
                                                      "Id",
                                                      creationDTO.getShopId())));
        PaymentHistory paymentHistory =
                new PaymentHistory(orderInfo, creationDTO.getFullName(), creationDTO.getShopId(), totalPrice,
                                   EPaymentCategory.SHOP_PRICE, EPaymentType.VNPAY, payment.getId());
        paymentHistory.setRedirectUrl(creationDTO.getRedirectUrl());
        paymentHistory.setParameter(String.format("{\"shopPriceId\":\"%s\"}", creationDTO.getShopPriceId()));
        this.paymentHistoryRepo.save(paymentHistory);

        return payment;
    }

    @Override
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }
}
