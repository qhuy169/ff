package gt.electronic.ecommerce.models.interfaces;

import lombok.Data;

/**
 * @author quang huy
 * @created 03/12/2025 - 11:57 AM
 */
public interface IInfoRating {
  Long getTotalVote();

  int getStar();
}
