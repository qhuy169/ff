package gt.electronic.ecommerce.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import gt.electronic.ecommerce.entities.*;
import gt.electronic.ecommerce.exceptions.ResourceNotValidException;
import gt.electronic.ecommerce.models.clazzs.OrderLog;
import gt.electronic.ecommerce.models.enums.EDiscountType;
import gt.electronic.ecommerce.models.enums.EPattern;
import gt.electronic.ecommerce.models.enums.ETimeDistance;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author quang huy
 * @created 07/03/2025 - 11:14 PM
 */
public class Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class.getName());

    // Regex
    public static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$";
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final String REGEX_PHONE = "^(0|84?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$";

    // Response
    public static final String OBJECT_EXISTED = "%s is existed";
    public static final String OBJECT_EXISTED_BY_FIELD = "%s is existed with %s = %s";
    public static final String OBJECT_EXISTED_BY_TWO_FIELD = "%s is existed with %s = %s and %s = %s";
    public static final String OBJECT_NOT_FOUND = "Not found %s!";
    public static final String OBJECT_NOT_FOUND_BY_FIELD = "Not found %s by %s = %s";

    public static final String OBJECT_NOT_FOUND_BY_TWO_FIELD = "Not found %s by %s = %s and %s = %s";
    public static final String NOT_FOUND_OBJECT_VALID = "Not found %s by %s = %s is valid";
    public static final String OBJECT_NOT_CHILD = "%s with %s = %s is not a child of %s with %s = %s";

    public static final String FIELD_NOT_BLANK = "Field %s not blank";
    public static final String FIELD_NOT_VALID = "Field %s not valid";
    public static final String BOTH_FIELDS_NOT_BLANK = "Both field %s or %s not blank";
    public static final String DISCOUNT_INVALID = "Invalid discount code";
    public static final String INVALID_FILED = "Invalid %s with % = %s";
    public static final String INVALID_TWO_FIELD = "Invalid %s with %s = %s  and %s = %s";
    public static final String DISCOUNT_CODE_EXPIRED = "Expired discount code";
    public static final String DISCOUNT_NOT_STARTED = "the discount hasn't started yet";
    public static final String DISCOUNT_USED_UP = "The discount code has been used up";
    public static final String PRODUCT_NOT_ENOUGH = "Product quantity is not enough";
    public static final String USER_NOT_PERMISSION = "User does not have permission to do this";
    public static final String USER_NOT_PRESENT = "User not present";
    public static final String ACTION_SUCCESSFULLY = "This action successfully";
    public static final String GET_ALL_OBJECT_SUCCESSFULLY = "Get all %ss  successfully!";
    public static final String REGISTER_USER_SUCCESSFULLY = "Register User with phone = %s successfully!";
    public static final String CREATE_OBJECT_SUCCESSFULLY = "Create new %s successfully!";
    public static final String CREATE_MAIN_OBJECT_SUCCESSFULLY = "Create new main %s successfully!";
    public static final String CREATE_RELY_OBJECT_SUCCESSFULLY = "Create new rely %s successfully!";
    public static final String UPDATE_OBJECT_SUCCESSFULLY = "Update %s successfully!";
    public static final String UPDATE_MAIN_OBJECT_SUCCESSFULLY = "Update new main %s successfully!";
    public static final String UPDATE_RELY_OBJECT_SUCCESSFULLY = "Update new rely %s successfully!";
    public static final String DELETE_OBJECT_SUCCESSFULLY = "Delete %s successfully!";
    public static final String ADD_ALL_OBJECT_TO_OBJECT_SUCCESSFULLY = "Add all %s to %s successfully!";
    public static final String REMOVE_ALL_OBJECT_FROM_OBJECT_SUCCESSFULLY = "Remove all %s from %s successfully!";

    // INFO
    public static final String PAYMENT_ORDER = "%s thanh toan don hang %s voi tong tien %s voi %s";
    public static final String LOG_UPDATE_PAYMENT_HISTORY_ORDER = "%s update PaymentHistory with paymentCode = %s when paying bill Order with orderId = %s with %s at %s";
    public static final String LOG_UPDATE_PAYMENT_HISTORY_SHOP_PRICE = "%s update PaymentHistory with paymentCode = %s when paying bill ShopPrice with shopId = %s and shopPriceId = %s with %s at %s";
    public static final String LOG_UPDATE_ORDER_SHIPMENT = "Update Order Shipment with id = %s to status = %s with log = %s by shipper with loginKey = %s";
    public static final String SUCCESS = "Successfully";
    public static final String FAIL = "Failed";
    public static final String ORDER_STATE_CHANGE_LOG = "Đơn hàng đã chuyển từ trạng thái %s sang trạng thái %s !";

    // Log
    public static final String LOG_GET_ALL_OBJECT = "Fetching all %ss";
    public static final String LOG_GET_OBJECT = "Fetching %s with %s = %s";
    public static final String LOG_GET_OBJECT_BY_USER = "Fetching %s with %s = %s";
    public static final String LOG_GET_ALL_OBJECT_BY_FIELD = "Fetching all %ss with %s = %s";
    public static final String LOG_GET_ALL_OBJECT_BY_TWO_FIELD = "Fetching all %ss with %s = %s and %s = %s";
    public static final String LOG_GET_ALL_OBJECT_BY_THREE_FIELD = "Fetching all %ss with %s = %s and %s = %s and %s = %s";
    public static final String AND_FIELD = " and %s = %s";
    public static final String LOG_CREATE_OBJECT = "Creating new %s with %s = %s to the database";
    public static final String LOG_REGISTER_OBJECT = "Registering new %S with %s = %s to database";
    public static final String LOG_CREATE_OBJECT_BY_TWO_FIELD = "Creating new %s with %s = %s and %s = %s to the database";
    public static final String LOG_UPDATE_OBJECT = "Updating %s with %s = %s to the database";
    public static final String LOG_UPDATE_OBJECT_BY_TWO_FIELD = "Updating %s with %s = %s and %s = %s to the database";
    public static final String LOG_DELETE_OBJECT = "Deleting %s with %s = %s from the database";
    public static final String LOG_ADD_ALL_OBJECT_TO_OBJECT = "Add all %s : %s to %s with %s = %s";
    public static final String LOG_REMOVE_ALL_OBJECT_FROM_OBJECT = "Remove all %s : %s from %s with %s = %s";
    public static final String ADD_LOG_FOR_USER = " for User with %s = %s";
    public static final String LOG_GET_STATISTIC_BY_USER = "Statistic %s for %s with loginKey = %s";
    public static final String LOG_GET_ALL_OBJECT_BY_USER = "Fetching all %s for %s with loginKey = %s";
    public static final String LOG_UPDATE_PRODUCT_BLACK_LIST_AT = "Update product black list for all server at %s";
    public static final String LOG_CHECK_SHOP_PRICE_PACKAGE = "Check shop price package for all server at %s";
    public static final String LOG_SEND_EMAIL_FOR_MESSAGE = "Send email from messages for all server at %s";
    public static final String LOG_RECEIVE_ORDER_SHIPMENTS_BY_SHIPPER = "Receive list order shipments %s by shipper with loginKey = %s";

    // Length
    public static final int LENGTH_DISCOUNT_CODE_GENERATE = 10;
    public static final int LENGTH_USERNAME_GENERATE = 20;
    public static final int LENGTH_PASSWORD_GENERATE = 16;
    public static final int DEFAULT_GENERATE_LENGTH = 8;
    // public static final String DEFAULT_PASSWORD = "Abc@1234567890";
    public static final String DEFAULT_PASSWORD = "12345";

    // Objects Per Page
    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_SIZE = "50";
    public static final String PRODUCT_PER_PAGE = "40";
    public static final String SHOP_PER_PAGE = "40";
    public static final String DISCOUNT_PER_PAGE = "40";
    public static final String IMAGE_PER_PAGE = "50";
    public static final String USERS_PER_PAGE = "40";
    public static final String COMMENT_PER_PAGE = "100";
    public static final String FEEDBACK_PER_PAGE = "300";
    public static final String DEFAULT_FIRST_NAME = "Danh";
    public static final String DEFAULT_LAST_NAME = "Ẩn";

    //
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    //
    public static final String PRE_API_AUTH = "auth";
    public static final String PRE_API_BRAND = "/api/v1/brands";
    public static final String PRE_API_CATEGORY = "/api/v1/categories";
    public static final String PRE_API_CART = "/api/v1/carts";
    public static final String PRE_API_COMMENT = "/api/v1/comments";
    public static final String PRE_API_FEEDBACK = "/api/v1/feedbacks";
    public static final String PRE_API_IMAGE = "/api/v1/images";
    public static final String PRE_API_PAYMENT = "/api/v1/payment";
    public static final String PRE_API_PRODUCT = "/api/v1/products";
    public static final String PRE_PAYPAL_CANCEL_URL = "/paypal/pay/cancel";
    public static final String PRE_PAYPAL_SUCCESS_URL = "/paypal/pay/success";
    //
    public static final String IMAGE_DEFAULT_PATH = "IMAGE_OTHER/l9faer7pevfo5kgs7zztubvgt9ikxy4u.jpg";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public static final String TRANSACTION_STATUS = "transaction_status";
    public static final int cookieExpireSeconds = 600;
    // public static final long checkBlackListSeconds = 1000 * 3600 * 24 * 7;
    // @Value("${app.shop.default.timeCheckBlackProductMs}")
    public static final long timeCheckBlackProductMs = 604800000; // 1Week
    public static final Gson gson = new Gson();
    public static final ObjectMapper mapper = new ObjectMapper();

    public static String toSlug(String input) {
        String noWhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    @Getter
    @Setter
    public static class TimeDistance {
        public String timeDistance;
        private boolean isUpdated;
    }

    public static TimeDistance getTimeDistance(Date createAt, Date updateAt) {
        TimeDistance timeDistance = new TimeDistance();
        Date lastDate;
        if (updateAt != null && createAt != null && updateAt.after(createAt)) {
            lastDate = updateAt;
            timeDistance.setUpdated(true);
        } else {
            lastDate = createAt;
            timeDistance.setUpdated(false);
        }

        Date currentDate = new Date();

        assert lastDate != null;
        long getDiff = currentDate.getTime() - lastDate.getTime();
        long timeMinute = 1000 * 60;
        long timeHour = 1000 * 60 * 60;
        long timeDay = timeHour * 24;
        long timeWeek = timeDay * 7;
        long timeMonth = timeDay * 30;
        long timeYear = timeDay * 365;
        if (getDiff < timeMinute) {
            timeDistance.setTimeDistance("Vừa mới đây");
        } else if (getDiff < timeHour) {
            timeDistance.setTimeDistance("Khoảng " + getDiff / (timeMinute) + " phút trước");
        } else if (getDiff < timeDay) {
            timeDistance.setTimeDistance("Khoảng " + getDiff / (timeHour) + " giờ trước");
        } else if (getDiff < timeWeek) {
            timeDistance.setTimeDistance("Khoảng " + getDiff / (timeDay) + " ngày trước");
        } else if (getDiff < timeMonth) {
            timeDistance.setTimeDistance("Khoảng " + getDiff / (timeWeek) + " tuần trước");
        } else if (getDiff < timeYear) {
            timeDistance.setTimeDistance("Khoảng " + getDiff / (timeMonth) + " tháng trước");
        } else {
            timeDistance.setTimeDistance("Khoảng " + getDiff / (timeYear) + " năm trước");
        }
        return timeDistance;
    }

    public static Location getLocationFromLocationString(String locationString) {
        Location location = new Location();
        int commaIndex = locationString.lastIndexOf(",");
        if (commaIndex != -1) {
            location.setProvince(locationString.substring(commaIndex + 1).trim());
            locationString = locationString.substring(0, commaIndex);
            commaIndex = locationString.lastIndexOf(",");
            if (commaIndex != -1) {
                location.setDistrict(locationString.substring(commaIndex + 1).trim());
                locationString = locationString.substring(0, commaIndex);
                location.setCommune(locationString);
            } else {
                location.setDistrict(locationString.trim());
            }
        } else {
            location.setProvince(locationString.trim());
        }
        return location;
    }

    public static String getLocationStringFromLocation(Location location) {
        String locationString = "";
        if (location.getProvince() != null) {
            locationString = location.getProvince();
            if (location.getDistrict() != null) {
                locationString = location.getDistrict() + ", " + locationString;
                if (location.getCommune() != null) {
                    locationString = location.getCommune() + ", " + locationString;
                }
            }
        }
        return locationString;
    }

    public static String getLocationStringFromLocationAndLine(Location location, String line) {
        String locationString = "";
        if (location.getProvince() != null) {
            locationString = location.getProvince();
            if (location.getDistrict() != null) {
                locationString = location.getDistrict() + ", " + locationString;
                if (location.getCommune() != null) {
                    locationString = location.getCommune() + ", " + locationString;
                }
            }
        }
        return line + ", " + locationString;
    }

    public static String getUrlFromPathImage(String path) {
        if (path.startsWith("http")) {
            return path;
        }
        // // Local address
        // String hostAddress = InetAddress.getLocalHost().getHostAddress();
        // String hostName = InetAddress.getLocalHost().getHostName();
        // // Remote address
        // String remoteAddress = InetAddress.getLoopbackAddress().getHostAddress();
        // String remoteName = InetAddress.getLoopbackAddress().getHostName();
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + PRE_API_IMAGE + "/" + path;
    }

    public static String getFullNameFromLastNameAndFirstName(String lastName, String firstName) {
        String fullName = lastName != null ? lastName : "";
        fullName = fullName + " " + (firstName != null ? firstName : "");
        return fullName.trim();
    }

    public static boolean checkValidDiscount(Discount discount) {
        if (discount.getQuantity() < 1) {
            throw new ResourceNotValidException(String.format(Utils.DISCOUNT_USED_UP));
        }

        return checkValidDate(discount.getStartDate(), discount.getEndDate(), false);
    }

    public static boolean isAvailableDiscount(Discount discount) {
        if (discount.getQuantity() < 1) {
            throw new ResourceNotValidException(String.format(Utils.DISCOUNT_USED_UP));
        }

        return checkValidDate(discount.getStartDate(), discount.getEndDate(), true);
    }

    public static boolean checkValidDate(Date startDate, Date endDate, boolean isAvailable) {
        Date currentDate = new Date();
        if (!isAvailable && startDate != null && startDate.before(currentDate)) {
            throw new ResourceNotValidException(String.format(Utils.DISCOUNT_NOT_STARTED));
        } else if (endDate != null && endDate.before(currentDate)) {
            throw new ResourceNotValidException(String.format(Utils.DISCOUNT_CODE_EXPIRED));
        }
        return true;
    }

    public static BigDecimal getPriceProduct(Product product, Sale sale, Long... quantity) {
        BigDecimal price;
        Double percent = 0d;
        if (sale != null) {
            percent = sale.getPercent();
        }
        Long count = 1L;
        if (quantity.length > 0) {
            count = quantity[0];
        }
        price = product.getPrice().multiply(BigDecimal.valueOf((1 - percent) * count))
                .setScale(0, RoundingMode.HALF_UP);

        return price;
    }

    public static final Double toBeTruncatedDouble(Double number, int... scales) {
        int scale = 2;
        if (scales.length > 0) {
            scale = scales[0];
        }
        return BigDecimal.valueOf(number)
                .setScale(scale, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public static BigDecimal getTotalPriceFromOrderItems(Set<OrderItem> orderItemSet, Long shopId) {
        BigDecimal totalPrice = new BigDecimal(0);
        if (orderItemSet != null) {
            for (OrderItem orderItem : orderItemSet) {
                if (shopId == null || Objects.equals(orderItem.getProduct().getShop().getId(), shopId)) {
                    totalPrice = totalPrice
                            .add(orderItem.getTotalPrice().multiply(new BigDecimal(orderItem.getQuantity())));
                }
            }
        }
        return totalPrice;
    }

    public static EPattern isPattern(String key) {
        if (key.matches(Utils.REGEX_PHONE)) {
            return EPattern.PHONE;
        } else if (key.matches(Utils.REGEX_EMAIL)) {
            return EPattern.EMAIl;
        } else if (key.matches(Utils.REGEX_PASSWORD)) {
            return EPattern.PASSWORD;
        } else {
            return EPattern.NONE;
        }
    }

    // public static boolean isValidDate(Date startDate, Date endDate) {
    // Date currentDate = new Date();
    // return currentDate.after(startDate) && currentDate.before(endDate);
    // }

    public static String[] getFirstNameAndLastNameFromFullName(String fullName) {
        fullName = fullName.replaceAll("\\s", " ").trim();
        int firstSpace = fullName.indexOf(" ");
        if (firstSpace == -1) {
            return new String[] { fullName, "" };
        } else {
            String firstName = fullName.substring(0, firstSpace);
            String lastName = fullName.substring(firstSpace + 1);
            return new String[] { firstName, lastName };
        }
    }

    public static OrderShop getOrderShopFromList(Set<OrderShop> orderShops, Shop shop, Order order) {
        OrderShop orderShop = null;
        for (OrderShop item : orderShops) {
            if (item.getShop() == shop) {
                orderShop = item;
            }
        }
        if (orderShop != null) {
            orderShops.remove(orderShop);
        }
        orderShop = new OrderShop(order, shop);
        return orderShop;
    }

    // Convert Date to Calendar
    public static Calendar dateToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }

    // Convert Calendar to Date
    public static Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }

    public static String getStringFromCalendar(Calendar calendar, ETimeDistance timeDistance) {
        DateFormat sdfDay = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat sdfMonth = new SimpleDateFormat("MM/yyyy");
        DateFormat sdfYear = new SimpleDateFormat("yyyy");
        switch (timeDistance) {
            case MONTH:
                return sdfMonth.format(calendar.getTime());
            case YEAR:
                return sdfYear.format(calendar.getTime());
            default:
                return sdfDay.format(calendar.getTime());
        }
    }

    public static String generateUsername(String firstName, String lastName) {
        if (!Objects.equals(firstName, "") || !Objects.equals(lastName, "")) {
            return vnToSlug(lastName + firstName, true) + getRandomNumberString(4);
        } else {
            return "user100" + getRandomNumberString(6);
        }
    }

    public static String getRandomNumberString(int digit) {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        long number = rnd.nextLong((long) (Math.pow(10, digit + 1) - 1));

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public static String vnToSlug(String title, boolean... isUsername) {
        // Đổi chữ hoa thành chữ thường
        String slug = title.toLowerCase();

        // Đổi ký tự có dấu thành không dấu
        slug = slug.replaceAll("(?i)(á|à|ả|ạ|ã|ă|ắ|ằ|ẳ|ẵ|ặ|â|ấ|ầ|ẩ|ẫ|ậ)", "a");
        slug = slug.replaceAll("(?i)(é|è|ẻ|ẽ|ẹ|ê|ế|ề|ể|ễ|ệ)", "e");
        slug = slug.replaceAll("(?i)(i|í|ì|ỉ|ĩ|ị)", "i");
        slug = slug.replaceAll("(?i)(ó|ò|ỏ|õ|ọ|ô|ố|ồ|ổ|ỗ|ộ|ơ|ớ|ờ|ở|ỡ|ợ)", "o");
        slug = slug.replaceAll("(?i)(ú|ù|ủ|ũ|ụ|ư|ứ|ừ|ử|ữ|ự)", "u");
        slug = slug.replaceAll("(?i)(ý|ỳ|ỷ|ỹ|ỵ)", "y");
        slug = slug.replaceAll("(?i)(đ)", "d");
        String noWhitespace;
        if (isUsername.length > 0 && isUsername[0]) {
            noWhitespace = WHITESPACE.matcher(slug).replaceAll("");
        } else {
            noWhitespace = WHITESPACE.matcher(slug).replaceAll("-");
        }
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        slug = NONLATIN.matcher(normalized).replaceAll("");
        // Xóa các ký tự đặt biệt
        slug = slug.replaceAll(
                "(?i)(\\`|\\~|\\!|\\@|\\#|\\||\\$|\\%|\\^|\\&|\\*|\\(|\\)|\\+|\\=|\\,|\\.|\\/|\\?|\\>|\\<|'|\"|\\:|\\;|_)",
                "");
        // Đổi khoảng trắng thành ký tự gạch ngang
        slug = slug.replaceAll("(?i)( )", "-");
        // Đổi nhiều ký tự gạch ngang liên tiếp thành 1 ký tự gạch ngang
        // Phòng trường hợp người nhập vào quá nhiều ký tự trắng
        slug = slug.replaceAll("(?i)(\\-\\-\\-\\-\\-)", "-");
        slug = slug.replaceAll("(?i)(\\-\\-\\-\\-)", "-");
        slug = slug.replaceAll("(?i)(\\-\\-\\-)", "-");
        slug = slug.replaceAll("(?i)(\\-\\-)", "-");
        // Xóa các ký tự gạch ngang ở đầu và cuối
        slug = '@' + slug + '@';
        slug = slug.replaceAll("(?i)(\\@\\-|\\-\\@|\\@)", "");
        return slug;
    }

    public static double getRoundingDigit(double digit, int scale) {
        double rangeScale = Math.pow(10, scale);
        return (double) Math.round(digit * rangeScale) / rangeScale;
    }

    public static BigDecimal getRoundingDigit(BigDecimal digit, int scale, boolean... isRound) {
        if (isRound.length > 0 && isRound[0]) {
            MathContext m = new MathContext(scale + 1, RoundingMode.HALF_UP);
            return digit.round(m);
        } else {
            return digit.setScale(0, RoundingMode.HALF_UP);
        }
    }

    public static final String addLogToOrderShop(OrderLog orderLog, String originLogs) {
        List<OrderLog> orderLogs;
        if (originLogs == null || originLogs.isEmpty()) {
            orderLogs = Collections.singletonList(orderLog);
        } else {
            orderLogs = new LinkedList<>(Arrays.asList(gson.fromJson(originLogs, OrderLog[].class)));
            orderLogs.add(orderLog);
        }
        return gson.toJson(orderLogs);
    }
}
