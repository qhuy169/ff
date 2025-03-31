package gt.electronic.ecommerce.dto.request;

import gt.electronic.ecommerce.models.enums.EProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:21 AM
 * @project gt-backend
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreationDTO {
  private String name;
  private BigDecimal price;
  private Long quantity;
  private Long shopId;
  private EProductStatus status;
  private Integer brandId;
  private Integer categoryId;
  // private DescriptionCreationDTO[] descriptions;
  private Map<String, String> descriptions;
  private String location;
  private String description;
}
