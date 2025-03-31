package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.models.enums.EGender;
import gt.electronic.ecommerce.models.enums.EOrderStatus;
import gt.electronic.ecommerce.models.enums.EPaymentType;
import gt.electronic.ecommerce.utils.Utils;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author quang huy
 * @created 09/09/2025 - 1:39 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private EGender gender;

    @Column(name = "full_name", length = 45)
    @NotNull(message = "An fullName is required!")
    private String fullName;

    @Column(name = "email", length = 320)
    @Size(message = "Invalid email size.", max = 320, min = 10)
    // @NotNull(message = "An email is required!")
    @Pattern(regexp = (Utils.REGEX_EMAIL), message = "Invalid email")
    private String email;

    @Column(name = "phone", length = 13)
    @Size(message = "Invalid phone size.", max = 13, min = 9)
    @NotNull(message = "An phone is required!")
    @Pattern(regexp = (Utils.REGEX_PHONE), message = "Invalid phone")
    private String phone;

    @Column(name = "line", nullable = false)
    @NotNull(message = "An line is required!")
    private String line;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment", length = 50, nullable = false)
    @NotNull(message = "An payment is required!")
    private EPaymentType payment;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "shipping_method_id", nullable = false)
    // private ShippingMethod shippingMethod;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "tbl_order_discounts", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "discount_id"))
    private Set<Discount> discounts = new HashSet<>();

    // @Column(name = "pay_at")
    // @Temporal(TemporalType.TIMESTAMP)
    // private Date payAt;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "payment_order_code")
    private String paymentOrderCode;

    // @Column(name = "ship_order_code")
    // private String shipOrderCode;

    // @Column(name = "log", length = 500)
    // private String log;

    // @Column(name = "expected_delivery_time")
    // @Temporal(TemporalType.TIMESTAMP)
    // private Date expectedDeliveryTime;

    @Column(name = "total_price_product", nullable = false)
    @NotNull(message = "An totalPriceProduct is required!")
    @DecimalMin(value = "0", message = "An totalPriceProduct must be greater than or equal to 0.")
    private BigDecimal totalPriceProduct = new BigDecimal(0);

    @Column(name = "total_price_discount", nullable = false)
    @NotNull(message = "An totalPriceDiscount is required!")
    @DecimalMin(value = "0", message = "An totalPriceDiscount must be greater than or equal to 0.")
    private BigDecimal totalPriceDiscount = new BigDecimal(0);

    @Column(name = "total_fee", nullable = false)
    @NotNull(message = "An totalFee is required!")
    private BigDecimal totalFee = new BigDecimal(0);

    @Column(name = "total_price", nullable = false)
    @NotNull(message = "An totalPrice is required!")
    @DecimalMin(value = "0", message = "An totalPrice must be greater than or equal to 0.")
    private BigDecimal totalPrice = new BigDecimal(0);

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    @NotNull(message = "An status is required!")
    private EOrderStatus status = EOrderStatus.ORDER_SHIPPING;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OrderShop> orderShops = new HashSet<>();

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    @PreRemove
    private void preRemove() {
        this.discounts = new HashSet<>();
    }
}
