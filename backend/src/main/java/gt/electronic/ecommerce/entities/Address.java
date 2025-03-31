package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.entities.keys.AddressKey;
import gt.electronic.ecommerce.models.enums.EAddressType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author quang huy
 * @created 18/09/2025 - 8:44 AM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_address")
public class Address {
  @EmbeddedId
  private AddressKey id;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @MapsId("locationId")
  @JoinColumn(name = "location_id")
  private Location location;

  @Column(name = "line", nullable = false)
  @NotNull(message = "An line is required!")
  private String line;

  @Enumerated(EnumType.STRING)
  @Column(name = "address_type", length = 50, nullable = false)
  @NotNull(message = "An addressType is required!")
  private EAddressType addressType;

  @Column(name = "is_default", nullable = false, columnDefinition = "boolean default false")
  private boolean isDefault;

  public Address(User user, Location location, String line) {
    this.id = new AddressKey(user.getId(), location.getId());
    this.user = user;
    this.location = location;
    this.line = line;
    this.addressType = EAddressType.ADDRESS_HOME;
    this.isDefault = true;
  }
}
