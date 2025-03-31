package gt.electronic.ecommerce.entities;

import gt.electronic.ecommerce.dto.request.AddressCreationDTO;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author quang huy
 * @created 09/09/2025 - 1:39 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_location")
public class Location {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String commune;

  private String district;

  @Column(name = "province", nullable = false)
  @NotNull(message = "An province is required!")
  private String province;

  public Location(String province) {
    this.province = province;
  }

  public Location(String commune, String district, String province) {
    this.commune = commune;
    this.district = district;
    this.province = province;
  }

  public Location(AddressCreationDTO address) {
    this.commune = address.getWard();
    this.district = address.getDistrict();
    this.province = address.getCity();
  }
}
