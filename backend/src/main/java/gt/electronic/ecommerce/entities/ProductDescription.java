package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.entities.keys.ProductDescriptionKey;
import lombok.*;

import javax.persistence.*;

/**
 * @author quang huy
 * @created 11/09/2025 - 8:01 PM
 * @project gt-backend
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_product_description")
public class ProductDescription {
  @EmbeddedId
  private ProductDescriptionKey id;

  @ManyToOne
  @MapsId("productId")
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne
  @MapsId("descriptionId")
  @JoinColumn(name = "description_id")
  private Description description;

  @Column(length = 5000, nullable = false)
  private String value;
}
