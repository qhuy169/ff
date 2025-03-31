package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.models.enums.EPaymentCategory;
import gt.electronic.ecommerce.models.enums.EPaymentType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_payment_history")
public class PaymentHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String info;

    private String fullName;

    private Long entityId;

    private String parameter;

    private BigDecimal totalPrice;

    private Boolean isSuccess;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 50, nullable = false)
    @NotNull(message = "An category is required!")
    private EPaymentCategory category = EPaymentCategory.ORDER;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50, nullable = false)
    @NotNull(message = "An type is required!")
    private EPaymentType type = EPaymentType.VNPAY;

    private String paymentCode;

    private String redirectUrl;

    @Column(columnDefinition = "DATETIME(6)")
    private Date payAt;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    public PaymentHistory(String info,
                          String fullName,
                          Long entityId,
                          BigDecimal totalPrice,
                          EPaymentCategory category,
                          EPaymentType type,
                          String paymentCode) {
        this.info = info;
        this.fullName = fullName;
        this.entityId = entityId;
        this.totalPrice = totalPrice;
        this.category = category;
        this.type = type;
        this.paymentCode = paymentCode;
    }
    public void update(Boolean isSuccess, Date payAt) {
        this.isSuccess = isSuccess;
        this.payAt = payAt;
    }
}
