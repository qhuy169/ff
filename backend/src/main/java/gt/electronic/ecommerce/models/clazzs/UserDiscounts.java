package gt.electronic.ecommerce.models.clazzs;

import gt.electronic.ecommerce.entities.Discount;
import gt.electronic.ecommerce.entities.User;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author quang huy
 * @created 26/11/2025 - 9:18 PM
 */
@Data
@ToString
public class UserDiscounts {
  Long userId;
  List<String> discountCodes;
}
