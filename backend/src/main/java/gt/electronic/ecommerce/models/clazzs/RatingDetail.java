package gt.electronic.ecommerce.models.clazzs;

import lombok.*;

/**
 * @author quang huy
 * @created 04/10/2025 - 6:05 PM
 * @project gt-backend
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RatingDetail {
  private int star;
  private long totalVote;
  private int percent;
}
