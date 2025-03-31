package gt.electronic.ecommerce.models.clazzs;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author quang huy
 * @created 30/11/2025 - 12:22 PM
 */
@Data
@ToString
public class DiscountCodes {
  private List<String> discountCodes;
}
