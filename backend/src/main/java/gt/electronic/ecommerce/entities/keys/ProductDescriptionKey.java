package gt.electronic.ecommerce.entities.keys;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author quang huy
 * @created 11/09/2025 - 8:07 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProductDescriptionKey implements Serializable {
  @Column(name = "product_id")
  Long productId;

  @Column(name = "description_id")
  Long descriptionId;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof ProductDescriptionKey))
      return false;
    ProductDescriptionKey that = (ProductDescriptionKey) o;
    return this.getProductId().equals(that.getProductId()) && this.getDescriptionId().equals(that.getDescriptionId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getProductId(), this.getDescriptionId());
  }
}
