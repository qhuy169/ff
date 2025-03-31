package gt.electronic.ecommerce.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import gt.electronic.ecommerce.config.AppProperties;
import gt.electronic.ecommerce.dto.request.PaypalCreationDTO;
import gt.electronic.ecommerce.dto.request.PaypalForShopPriceCreationDTO;
import gt.electronic.ecommerce.dto.request.VNPayCreationDTO;
import gt.electronic.ecommerce.dto.request.VNPayForShopPriceCreationDTO;
import gt.electronic.ecommerce.dto.response.PaymentUrlResponseDTO;
import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.models.enums.PaypalPaymentIntent;
import gt.electronic.ecommerce.models.enums.PaypalPaymentMethod;
import gt.electronic.ecommerce.services.OrderService;
import gt.electronic.ecommerce.services.PaymentService;
import gt.electronic.ecommerce.services.PaypalService;
import gt.electronic.ecommerce.services.VNPayService;
import gt.electronic.ecommerce.utils.CookieUtils;
import gt.electronic.ecommerce.config.VNPayConfig;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static gt.electronic.ecommerce.utils.Utils.*;

/**
 * @author quang huy
 * @created 17/11/2025 - 4:37 PM
 */
@RestController
@RequestMapping(value = "/api/v1/payment")
@CrossOrigin("*")
public class PaymentController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String PRE_SHOP_PRICE = "/shop-price";

    private OrderService orderService;

    @Autowired
    public void OrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    private PaymentService paymentService;

    @Autowired
    public void PaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    private PaypalService paypalService;

    @Autowired
    public void PaypalService(PaypalService paypalService) {
        this.paypalService = paypalService;
    }

    private VNPayService vnPayService;

    @Autowired
    public void VNPayService(VNPayService vnPayService) {
        this.vnPayService = vnPayService;
    }

    private AppProperties appProperties;

    @Autowired
    public void AppProperties(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @GetMapping("/vnpay/return")
    public String getVNPayReturn(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            fields.remove("vnp_SecureHashType");
            fields.remove("vnp_SecureHash");

            // Check checksum
            String signValue = VNPayConfig.hashAllFields(fields);
            // if (signValue.equals(vnp_SecureHash)) {

            boolean checkOrderId = true; // vnp_TxnRef exists in your database
            boolean checkAmount = true; // vnp_Amount is valid (Check vnp_Amount VNPAY returns compared to the amount of
                                        // the
            // code (vnp_TxnRef) in the Your database).
            boolean checkOrderStatus = true; // PaymnentStatus = 0 (pending)
            if (checkOrderId) {
                if (checkAmount) {
                    if (checkOrderStatus) {
                        String payString = request.getParameter("vnp_PayDate");
                        String paymentOrderCode = request.getParameter("vnp_TxnRef");
                        boolean success = true;
                        if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                            // Here Code update PaymnentStatus = 1 into your Database
                        } else {
                            success = false;
                            // Here Code update PaymnentStatus = 2 into your Database
                        }
                        // Optional<String> redirectUri = CookieUtils.getCookie(request,
                        // REDIRECT_URI_PARAM_COOKIE_NAME)
                        // .map(Cookie::getValue);
                        Date payAt = new Date();
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                            payAt = sdf.parse(payString);
                        } catch (ParseException ignored) {
                        }
                        // String targetUrl =
                        // redirectUri.orElse(appProperties.getOauth2().getAuthorizedRedirectUris().get(0));
                        String targetUrl = this.paymentService.updatePaymentHistory(paymentOrderCode, payAt, success)
                                .orElse(appProperties.getOauth2().getAuthorizedRedirectUris().get(0));

                        response.sendRedirect(UriComponentsBuilder.fromUriString(targetUrl)
                                .queryParam(TRANSACTION_STATUS, success).build().toUriString());
                        return "{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}";
                    } else {
                        return "{\"RspCode\":\"02\",\"Message\":\"Order already confirmed\"}";
                    }
                } else {
                    return "{\"RspCode\":\"04\",\"Message\":\"Invalid Amount\"}";
                }
            } else {
                return "{\"RspCode\":\"01\",\"Message\":\"Order not Found\"}";
            }
            // } else {
            // return "{\"RspCode\":\"97\",\"Message\":\"Invalid Checksum\"}";
            // }
        } catch (Exception e) {
            return "{\"RspCode\":\"99\",\"Message\":\"Unknow error\"}";
        }
    }

    @GetMapping("/vnpay-refund")
    public void getVNPayRefund(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // vnp_Command = refund
        String vnp_TxnRef = req.getParameter("vnp_TxnRef");
        String vnp_TransDate = req.getParameter("vnp_PayDate");
        String email = req.getParameter("vnp_Bill_Email");
        int amount = Integer.parseInt(req.getParameter("vnp_Amount")) * 100;
        String trantype = req.getParameter("trantype");
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "refund");
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Kiem tra ket qua GD OrderId:" + vnp_TxnRef);
        vnp_Params.put("vnp_TransDate", vnp_TransDate);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_CreateBy", email);
        vnp_Params.put("vnp_TransactionType", trantype);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        // Build data to hash and querystring
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_apiUrl + "?" + queryUrl;
        URL url = new URL(paymentUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String Rsp = response.toString();
        String respDecode = URLDecoder.decode(Rsp, "UTF-8");
        String[] responseData = respDecode.split("&|\\=");
        JsonObject job = new JsonObject();
        job.addProperty("data", Arrays.toString(responseData));
        Gson gson = new Gson();
        resp.getWriter().write(gson.toJson(job));
    }

    @PostMapping("/vnpay/create-payment-url")
    public ResponseObject<?> getPaymentUrlVNPay(
            @RequestBody @Valid VNPayCreationDTO vnPayCreationDTO,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {
        String ipAddress = VNPayConfig.getIpAddress(request);
        CookieUtils.addCookie(response,
                REDIRECT_URI_PARAM_COOKIE_NAME,
                vnPayCreationDTO.getRedirectUrl(),
                cookieExpireSeconds);
        return new ResponseObject<>(
                HttpStatus.OK,
                "Get url vnpay sucess",
                this.vnPayService.getPaymentUrlVNPay(ipAddress, vnPayCreationDTO));
    }

    @PostMapping("/vnpay/create-payment-url/shop-price")
    public ResponseObject<?> getPaymentUrlVNPayForShopPrice(
            @RequestBody @Valid VNPayForShopPriceCreationDTO vnPayCreationDTO,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {
        String ipAddress = VNPayConfig.getIpAddress(request);
        // CookieUtils.addCookie(response,
        // REDIRECT_URI_PARAM_COOKIE_NAME,
        // vnPayCreationDTO.getRedirectUrl(),
        // cookieExpireSeconds);
        return new ResponseObject<>(
                HttpStatus.OK,
                "Get url vnpay sucess",
                this.vnPayService.getPaymentUrlVNPayForShopPrice(ipAddress, vnPayCreationDTO));
    }

    @PostMapping("/paypal/create-payment-url")
    public ResponseObject<?> getPaymentUrlPaypal(@RequestBody @Valid PaypalCreationDTO creationDTO,
            HttpServletRequest request) {
        try {
            String cancelUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
                    + PRE_API_PAYMENT +
                    PRE_PAYPAL_CANCEL_URL;
            String successUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
                    + PRE_API_PAYMENT +
                    PRE_PAYPAL_SUCCESS_URL;
            Payment payment = this.paypalService.createPayment(
                    creationDTO,
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    cancelUrl,
                    successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    // return "redirect:" + links.getHref();
                    return new ResponseObject<>(
                            HttpStatus.OK,
                            "Get url paypal sucess",
                            new PaymentUrlResponseDTO(links.getHref(), new Date(), null));
                }
            }
        } catch (PayPalRESTException e) {
            this.LOGGER.error(e.getMessage());
        }
        return new ResponseObject<>(
                HttpStatus.BAD_REQUEST,
                "Get url paypal fail",
                null);
    }

    @GetMapping(PRE_PAYPAL_CANCEL_URL)
    public String cancelPaypal(@RequestParam("paymentId") String paymentOrderCode,
            @RequestParam("PayerID") String payerId,
            HttpServletResponse response) {
        String targetUrl = this.paymentService.updatePaymentHistory(paymentOrderCode, new Date(), false)
                .orElse(appProperties.getOauth2().getAuthorizedRedirectUris().get(0));
        // String targetUrl = "http://localhost:5000/";
        try {
            response.sendRedirect(UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam(TRANSACTION_STATUS, true).build().toUriString());
        } catch (IOException e) {
            this.LOGGER.error(e.getMessage());
        }
        return "{\"RspCode\":\"00\",\"Message\":\"Confirm Fail\"}";
    }

    @GetMapping(PRE_PAYPAL_SUCCESS_URL)
    public String successPaypal(@RequestParam("paymentId") String paymentOrderCode,
            @RequestParam("PayerID") String payerId,
            HttpServletResponse response) {
        try {
            Payment payment = this.paypalService.executePayment(paymentOrderCode, payerId);
            System.out.println(payment.getUpdateTime());
            if (payment.getState().equals("approved")) {
                String targetUrl = this.paymentService.updatePaymentHistory(paymentOrderCode, new Date(), true)
                        .orElse(appProperties.getOauth2().getAuthorizedRedirectUris().get(0));

                // String targetUrl = "http://localhost:5000/";

                response.sendRedirect(UriComponentsBuilder.fromUriString(targetUrl)
                        .queryParam(TRANSACTION_STATUS, true).build().toUriString());
                return "{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}";
            }
        } catch (PayPalRESTException | IOException e) {
            this.LOGGER.error(e.getMessage());
        }
        return "redirect:/";
    }

    @PostMapping("/paypal/create-payment-url/shop-price")
    public ResponseObject<?> getPaymentUrlPaypalForShopPrice(
            @RequestBody @Valid PaypalForShopPriceCreationDTO creationDTO,
            HttpServletRequest request) {
        try {
            String cancelUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
                    + PRE_API_PAYMENT +
                    PRE_PAYPAL_CANCEL_URL;
            String successUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
                    + PRE_API_PAYMENT +
                    PRE_PAYPAL_SUCCESS_URL;
            Payment payment = this.paypalService.createPaymentForShopPrice(
                    creationDTO,
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    cancelUrl,
                    successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    // return "redirect:" + links.getHref();
                    return new ResponseObject<>(
                            HttpStatus.OK,
                            "Get url paypal sucess",
                            new PaymentUrlResponseDTO(links.getHref(), new Date(), null));
                }
            }
        } catch (PayPalRESTException e) {
            this.LOGGER.error(e.getMessage());
        }
        return new ResponseObject<>(
                HttpStatus.BAD_REQUEST,
                "Get url paypal fail",
                null);
    }
}
