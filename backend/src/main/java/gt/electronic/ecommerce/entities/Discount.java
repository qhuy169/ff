package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.models.enums.EDiscountType;
import gt.electronic.ecommerce.config.CodeConfig;
import gt.electronic.ecommerce.utils.GenerateUtil;
import gt.electronic.ecommerce.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author quang huy
 * @created 09/09/2025 - 1:38 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_discount")
public class Discount {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", length = 100)
  // @NotNull(message = "An name is required!")
  @Size(message = "Invalid name size.", max = 100)
  private String name;

  @Column(name = "description", length = 300)
  @Size(message = "Invalid description size.", max = 300)
  private String description;

  @Column(name = "quantity", nullable = false)
  @NotNull(message = "An quantity is required!")
  @DecimalMin(value = "0", message = "Quantity must be greater than or equal to 0.")
  private Integer quantity;

  @Column(name = "percent")
  @DecimalMin(value = "0.01", message = "Percent must be greater than or equal to 0.01.")
  @DecimalMax(value = "1", message = "Percent must be smaller than or equal to 1.")
  private Double percent;

  @Column(name = "code", length = 10, nullable = false, unique = true)
  @NotNull(message = "An code is required!")
  @Size(message = "Invalid code size.", max = 10, min = 5)
  private String code;

  @Column(name = "capped_at")
  @DecimalMin(value = "0", message = "Capped At must be greater than or equal to 0.")
  @DecimalMax(value = "100000000", message = "Capped At must be smaller than or equal to 100 000 000.")
  private BigDecimal cappedAt;

  @Column(name = "price")
  @DecimalMin(value = "0", message = "Price must be  than or equal to 0.")
  private BigDecimal price;

  @Column(name = "min_spend", nullable = false)
  @NotNull(message = "An minSpeed is required!")
  @DecimalMin(value = "0", message = "Min Spend must be greater than or equal to 0.")
  private BigDecimal minSpend;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "shop_id", nullable = false)
  private Shop shop;

  @Column(name = "start_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date startDate;

  @Column(name = "end_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date endDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", length = 50, nullable = false)
  @NotNull(message = "An type is required!")
  private EDiscountType type = EDiscountType.DISCOUNT_SHOP_PRICE;

  @OneToOne(cascade = CascadeType.ALL)
  private Image thumbnail;

  @ManyToMany(mappedBy = "discounts")
  private Set<User> users = new HashSet<>();

  @Column(name = "created_at")
  @CreationTimestamp
  private Date createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Date updatedAt;

  public Discount(
      // String name,
      Integer quantity,
      Double percent,
      BigDecimal cappedAt,
      BigDecimal price,
      BigDecimal minSpend,
      Shop shop,
      EDiscountType type) {
    // this.name = name;
    this.quantity = quantity;
    this.code = GenerateUtil.generate(CodeConfig.length(Utils.LENGTH_DISCOUNT_CODE_GENERATE));
    this.percent = percent;
    this.cappedAt = cappedAt;
    this.price = price;
    this.minSpend = minSpend;
    this.shop = shop;
    this.startDate = new Date();
    long hourTime = 1000 * 3600;
    long dayTime = hourTime * 24;
    long monthTime = dayTime * 30;
    this.endDate = new Date(startDate.getTime() + monthTime);
    if (type != null) {
      this.type = type;
    } else {
      if (percent != null) {
        this.type = EDiscountType.DISCOUNT_SHOP_PERCENT;
      } else {
        this.type = EDiscountType.DISCOUNT_SHOP_PRICE;
      }
    }
  }
}
