package gt.electronic.ecommerce.dto.request;

import gt.electronic.ecommerce.entities.Address;
import gt.electronic.ecommerce.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * @author quang huy
 * @created 11/09/2025 - 4:57 PM
 * @project gt-backend
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressCreationDTO {
  // private String line;
  // private String commune;
  // private String district;
  // private String province;
  private String homeAdd;
  private String ward;
  private String district;
  private String city;

  public AddressCreationDTO(Set<Address> addresses) {
    if (addresses.iterator().hasNext()) {
      Address address = addresses.iterator().next();
      if (address != null) {
        this.homeAdd = address.getLine();
        this.ward = address.getLocation().getCommune();
        this.district = address.getLocation().getDistrict();
        this.city = address.getLocation().getProvince();
      }
    }
  }
}
