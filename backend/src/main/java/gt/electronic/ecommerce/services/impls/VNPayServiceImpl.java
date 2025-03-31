package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.dto.request.VNPayCreationDTO;
import gt.electronic.ecommerce.dto.request.VNPayForShopPriceCreationDTO;
import gt.electronic.ecommerce.dto.response.PaymentUrlResponseDTO;
import gt.electronic.ecommerce.entities.Order;
import gt.electronic.ecommerce.entities.PaymentHistory;
import gt.electronic.ecommerce.entities.Shop;
import gt.electronic.ecommerce.entities.ShopPrice;
import gt.electronic.ecommerce.exceptions.ResourceNotFoundException;
import gt.electronic.ecommerce.models.enums.EPaymentCategory;
import gt.electronic.ecommerce.models.enums.EPaymentType;
import gt.electronic.ecommerce.repositories.OrderRepository;
import gt.electronic.ecommerce.repositories.PaymentHistoryRepository;
import gt.electronic.ecommerce.repositories.ShopPriceRepository;
import gt.electronic.ecommerce.repositories.ShopRepository;
import gt.electronic.ecommerce.services.VNPayService;
import gt.electronic.ecommerce.utils.Utils;
import gt.electronic.ecommerce.config.VNPayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static gt.electronic.ecommerce.utils.Utils.OBJECT_NOT_FOUND_BY_FIELD;
import static gt.electronic.ecommerce.utils.Utils.PRE_API_PAYMENT;

/**
 * @author quang huy
 * @created 20/11/2025 - 6:59 PM
 */
@Service
@Transactional
public class VNPayServiceImpl implements VNPayService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String branchName = EPaymentType.VNPAY.name();

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
    public PaymentUrlResponseDTO getPaymentUrlVNPay(String ipAddress, VNPayCreationDTO creationDTO) {
        this.LOGGER.info(String.format(Utils.LOG_CREATE_OBJECT_BY_TWO_FIELD,
                branchName,
                "orderId",
                creationDTO.getOrderId(),
                "totalPrice",
                creationDTO.getTotalPrice()));
        PaymentUrlResponseDTO responseDTO = new PaymentUrlResponseDTO();
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String locate = "vn";
        if (creationDTO.getLocate() != null && !creationDTO.getLocate().isEmpty() &&
                !Objects.equals(creationDTO.getLocate()
                        .trim(), "")) {
            locate = creationDTO.getLocate();
        }
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);

        String vnp_OrderInfo = String.format(Utils.PAYMENT_ORDER,
                creationDTO.getFullName(),
                creationDTO.getOrderId(),
                creationDTO.getTotalPrice(),
                branchName);
        String orderType = "1";// req.getParameter("ordertype");
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        responseDTO.setCreateDate(cld.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        cld.add(Calendar.MINUTE, 30);
        responseDTO.setExpireDate(cld.getTime());
        String vnp_ExpireDate = formatter.format(cld.getTime());

        Order entityFound = this.orderRepo
                .findById(creationDTO.getOrderId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                        Order.class.getSimpleName(),
                                        "Id",
                                        creationDTO.getOrderId())));
        entityFound.setPaymentOrderCode(vnp_TxnRef);
        this.orderRepo.save(entityFound);

        PaymentHistory paymentHistory = new PaymentHistory(vnp_OrderInfo,
                creationDTO.getFullName(),
                creationDTO.getOrderId(),
                creationDTO.getTotalPrice(),
                EPaymentCategory.ORDER,
                EPaymentType.VNPAY,
                vnp_TxnRef);
        paymentHistory.setRedirectUrl(creationDTO.getRedirectUrl());
        this.paymentHistoryRepo.save(paymentHistory);

        Map<String, String> vnp_Params = new HashMap<>();

        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Locale", locate);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Amount",
                String.valueOf(creationDTO.getTotalPrice().multiply(new BigDecimal("100")).toBigInteger()));
        vnp_Params.put("vnp_ReturnUrl",
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + PRE_API_PAYMENT +
                        "/vnpay/return");
        vnp_Params.put("vnp_IpAddr", ipAddress);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        if (creationDTO.getBankCode() != null && !Objects.equals(creationDTO.getBankCode().trim(), "")) {
            vnp_Params.put("vnp_BankCode", creationDTO.getBankCode());
        }

        // Build data to hash and querystring
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        responseDTO.setPayUrl(paymentUrl);

        return responseDTO;
    }

    @Override
    public PaymentUrlResponseDTO getPaymentUrlVNPayForShopPrice(String ipAddress,
            VNPayForShopPriceCreationDTO creationDTO) {
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
        PaymentUrlResponseDTO responseDTO = new PaymentUrlResponseDTO();
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String locate = "vn";
        if (creationDTO.getLocate() != null && !creationDTO.getLocate().isEmpty() &&
                !Objects.equals(creationDTO.getLocate()
                        .trim(), "")) {
            locate = creationDTO.getLocate();
        }
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);

        String vnp_OrderInfo = String.format(Utils.PAYMENT_ORDER,
                creationDTO.getFullName(),
                "ShopPrice with ShopId = " + creationDTO.getShopId(),
                totalPrice,
                branchName);
        String orderType = "1";// req.getParameter("ordertype");
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        responseDTO.setCreateDate(cld.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        cld.add(Calendar.MINUTE, 15);
        responseDTO.setExpireDate(cld.getTime());
        String vnp_ExpireDate = formatter.format(cld.getTime());

        this.shopRepo
                .findById(creationDTO.getShopId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format(Utils.OBJECT_NOT_FOUND_BY_FIELD,
                                        Shop.class.getSimpleName(),
                                        "Id",
                                        creationDTO.getShopId())));
        PaymentHistory paymentHistory = new PaymentHistory(vnp_OrderInfo, creationDTO.getFullName(),
                creationDTO.getShopId(), totalPrice,
                EPaymentCategory.SHOP_PRICE, EPaymentType.VNPAY, vnp_TxnRef);
        paymentHistory.setRedirectUrl(creationDTO.getRedirectUrl());
        paymentHistory.setParameter(String.format("{\"shopPriceId\":\"%s\"}", creationDTO.getShopPriceId()));
        this.paymentHistoryRepo.save(paymentHistory);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Locale", locate);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_Amount", String.valueOf(new BigInteger("1000000")));
        // vnp_Params.put("vnp_Amount", String.valueOf(totalPrice.multiply(new
        // BigDecimal("100")).toBigInteger()));
        vnp_Params.put("vnp_ReturnUrl",
                ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + PRE_API_PAYMENT +
                        "/vnpay/return");
        vnp_Params.put("vnp_IpAddr", ipAddress);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        if (creationDTO.getBankCode() != null && !Objects.equals(creationDTO.getBankCode().trim(), "")) {
            vnp_Params.put("vnp_BankCode", creationDTO.getBankCode());
        }

        // Build data to hash and querystring
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        responseDTO.setPayUrl(paymentUrl);

        return responseDTO;
    }
}
