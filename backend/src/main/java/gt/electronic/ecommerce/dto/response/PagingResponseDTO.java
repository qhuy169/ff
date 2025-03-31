package gt.electronic.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * @author quang huy
 * @created 11/09/2025 - 11:11 AM
 * @project gt-backend
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagingResponseDTO {
  private Integer current_page;
  private List<T> data;
  private String first_page_url;
  private Integer from;
  private Integer last_page;
  private String last_page_url;
  private String next_page_url;
  private String path;
  private Integer per_page;
  private String prev_page_url;
  private Long to;
  private Long total;
}
