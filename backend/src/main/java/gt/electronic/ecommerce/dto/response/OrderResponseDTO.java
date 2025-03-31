package gt.electronic.ecommerce.dto.response;

import gt.electronic.ecommerce.models.clazzs.OrderLog;
import gt.electronic.ecommerce.models.enums.EOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author quang huy
 * @created 20/09/2025 - 9:27 PM
 * @project gt-backend
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private UserSimpleResponseDTO user;
    private int gender;
    private String fullName;
    private String email;
    private String phone;
    private AddressResponseDTO address;
    private int payment;
    private int shippingMethod;
    private BigDecimal totalPriceProduct;
    private BigDecimal totalPriceDiscount;
    private BigDecimal transportFee;
    private BigDecimal totalPrice;
    private String paymentOrderCode;
    private DiscountResponseDTO[] discounts;
    private int status;
    private Date payAt;
    private String note;
    private OrderDetailResponseDTO[] orderItems;
    private Date createdAt;
    private Date updatedAt;
    private OrderLog[] logs;

}
