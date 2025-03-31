package gt.electronic.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:18 AM
 * @project gt-backend
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiscountResponseDTO {
  private Long id;
  private String name;
  private String description;
  private Integer quantity;
  private Double percent;
  private String code;
  private BigDecimal cappedAt;
  private BigDecimal price;
  private BigDecimal minSpend;
  private Long shopId;
  private Date startDate;
  private Date endDate;
  private int type;
  private String thumbnail;
}
