package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.entities.keys.OrderShopKey;
import gt.electronic.ecommerce.models.enums.EOrderStatus;
import gt.electronic.ecommerce.models.enums.EShippingMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author quang huy
 * @created 03/12/2025 - 8:10 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_order_shop")
public class OrderShop {
    @EmbeddedId
    OrderShopKey key;

    @Generated(GenerationTime.INSERT)
    @Column(name = "internal_id", nullable = false, insertable = false, updatable = false, columnDefinition = "serial")
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @MapsId("shopId")
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @OneToMany(mappedBy = "orderShop", cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new HashSet<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
    }

    @Column(name = "pay_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date payAt;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "ship_order_code")
    private String shipOrderCode;

    @Lob
    private String log;

    @Column(name = "expected_delivery_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expectedDeliveryTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipping_method", length = 50, nullable = false)
    @NotNull(message = "An shippingMethod is required!")
    private EShippingMethod shippingMethod = EShippingMethod.GHN_EXPRESS;

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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "tbl_order_shop_discounts", joinColumns = {
            @JoinColumn(name = "order_id", referencedColumnName = "order_id"),
            @JoinColumn(name = "shop_id", referencedColumnName = "shop_id")
    }, inverseJoinColumns = @JoinColumn(name = "discount_id"))
    private Set<Discount> discounts = new HashSet<>();

    public void addDiscount(Discount discount) {
        discounts.add(discount);
    }

    public void removeDiscount(Discount discount) {
        discounts.remove(discount);
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    @NotNull(message = "An status is required!")
    private EOrderStatus status = EOrderStatus.ORDER_SHIPPING;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    @Override
    public int hashCode() {
        return this.key.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OrderShop) || getClass() != o.getClass())
            return false;
        OrderShop that = (OrderShop) o;
        return this.getKey().equals(that.getKey());
    }

    @PreRemove
    private void preRemove() {
        this.discounts = new HashSet<>();
    }

    public OrderShop(Order order, Shop shop) {
        this.key = new OrderShopKey(order.getId(), shop.getId());
        this.order = order;
        this.shop = shop;
    }
}
