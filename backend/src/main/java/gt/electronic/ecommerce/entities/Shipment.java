package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.models.enums.EOrderStatus;
import gt.electronic.ecommerce.models.enums.EShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_shipment")
public class Shipment {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    @JoinColumn(name = "shipper_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Long orderShopId;

    @Column(nullable = false)
    @NotNull(message = "An fromLine is required!")
    private String fromLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_location_id", nullable = false)
    private Location fromLocation;

    @Column(nullable = false)
    @NotNull(message = "An toLine is required!")
    private String toLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_location_id", nullable = false)
    private Location toLocation;

    @Column(nullable = false)
    @NotNull(message = "An total price is required!")
    @DecimalMin(value = "0", message = "Total price must be greater than or equal to 0.")
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    @NotNull(message = "An status is required!")
    private EShipmentStatus status = EShipmentStatus.SHIPPING;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    @Column(columnDefinition = "DATETIME(6)")
    private Date completedAt;

    public Shipment(User user,
                    Long orderShopId,
                    String fromLine,
                    Location fromLocation,
                    String toLine,
                    Location toLocation,
                    BigDecimal totalPrice) {
        this.user = user;
        this.orderShopId = orderShopId;
        this.fromLine = fromLine;
        this.fromLocation = fromLocation;
        this.toLine = toLine;
        this.toLocation = toLocation;
        this.totalPrice = totalPrice;
    }

    public Shipment(User user, OrderShop orderShop) {
        this.user = user;
        this.orderShopId = orderShop.getId();
        this.fromLine = orderShop.getShop().getLine();
        this.fromLocation = orderShop.getShop().getLocation();
        this.toLine = orderShop.getOrder().getLine();
        this.toLocation = orderShop.getOrder().getLocation();
        this.totalPrice = orderShop.getTotalFee();
    }
}
