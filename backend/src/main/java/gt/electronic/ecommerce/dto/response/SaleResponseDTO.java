package gt.electronic.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author quang huy
 * @created 16/09/2025 - 8:46 PM
 * @project gt-backend
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponseDTO {
  private Long id;
  private String name;
  private String description;
  private Double percent;
  private ProductGalleryDTO[] productGalleries;
  private UserSimpleResponseDTO creator;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date startDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date endDate;
  private String thumbnail;
}
