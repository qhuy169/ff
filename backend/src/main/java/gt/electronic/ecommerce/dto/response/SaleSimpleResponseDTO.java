package gt.electronic.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author quang huy
 * @created 08/10/2025 - 4:05 PM
 * @project gt-backend
 */
public class SaleSimpleResponseDTO {
  private Long id;
  private String name;
  private String description;
  private Double percent;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date startDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date endDate;
}
