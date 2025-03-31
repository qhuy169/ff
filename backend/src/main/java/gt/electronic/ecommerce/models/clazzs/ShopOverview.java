package gt.electronic.ecommerce.models.clazzs;

import lombok.Data;
import lombok.ToString;

/**
 * @author quang huy
 * @created 03/12/2025 - 12:05 PM
 */
@Data
@ToString
public class ShopOverview {
  private Long totalProduct;
  private Long totalVote;
  private Double avgStar;
  private String timeDistanceFromCreateAt;
}
