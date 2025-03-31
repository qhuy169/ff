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
 * @created 18/09/2025 - 8:48 AM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AddressKey implements Serializable {
  @Column(name = "user_id")
  Long userId;

  @Column(name = "location_id")
  Long locationId;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof AddressKey))
      return false;
    AddressKey that = (AddressKey) o;
    return this.getUserId().equals(that.getUserId()) && this.getLocationId().equals(that.getLocationId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getUserId(), this.getLocationId());
  }
}
