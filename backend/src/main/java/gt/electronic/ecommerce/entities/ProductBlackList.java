package gt.electronic.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tbl_product_black_list")
public class ProductBlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long productId;

    @Column(name = "product_name", length = 255, nullable = false)
    @NotNull(message = "An name is required!")
    @Size(message = "Invalid name size.", max = 255, min = 1)
    private String productName;

    @Column(name = "slug", length = 255, nullable = false)
    @Size(message = "Invalid slug size.", max = 300, min = 1)
    @NotNull(message = "An name is required!")
    private String slug = "";

    @Column(name = "img", length = 255, nullable = false)
    private String img = "";

    @Column
    private Long shopId;

    @Column(name = "scanAt")
    @CreationTimestamp
    private Date scanAt;

    @Column(name = "percent")
    @DecimalMin(value = "0.01", message = "Percent must be greater than or equal to 0.01.")
    @DecimalMax(value = "1", message = "Percent must be smaller than or equal to 1.")
    private double percent;

    @Column(name = "neg_total")
    private Long negTotal;

    @Column(name = "total")
    private Long total;

    @Column(name = "count", nullable = false, columnDefinition = "int default 1")
    private int count;

    @Column(name = "status", nullable = false, columnDefinition = "bit(1) default 1")
    @NotNull(message = "An status is required!")
    private boolean status = true;

    @Column(nullable = false, columnDefinition = "bit(1) default 0")
    @NotNull(message = "An isBaned is required!")
    private boolean isBaned = false;
}
