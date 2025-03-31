package gt.electronic.ecommerce.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:19 AM
 * @project gt-backend
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreationDTO {
  @NotNull(message = "An name is required!")
  private String name;
  private String description;
  private Integer parentCategoryId;
}
