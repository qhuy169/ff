package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.models.enums.EOrdertemStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author quang huy
 * @created 19/09/2025 - 9:27 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_order_item")
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "order_id", referencedColumnName = "order_id"),
      @JoinColumn(name = "shop_id", referencedColumnName = "shop_id")
  })
  private OrderShop orderShop;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sale_id")
  private Sale sale;

  @Column(name = "quantity", nullable = false)
  @NotNull(message = "An quantity is required!")
  @DecimalMin(value = "0", message = "Quantity must be greater than or equal to 0.")
  private Long quantity;

  @Column(name = "total_price", nullable = false)
  @NotNull(message = "An total price is required!")
  @DecimalMin(value = "0", message = "Total price must be greater than or equal to 0.")
  private BigDecimal totalPrice;

  @Column(name = "note", length = 500)
  private String note;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 50, nullable = false)
  @NotNull(message = "An status is required!")
  private EOrdertemStatus status = EOrdertemStatus.PAID;

  @Column(name = "created_at")
  @CreationTimestamp
  private Date createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Date updatedAt;
}
