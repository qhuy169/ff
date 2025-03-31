package gt.electronic.ecommerce.models.clazzs;

import lombok.*;

/**
 * @author quang huy
 * @created 19/09/2025 - 9:32 AM
 * @project gt-backend
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductRating {
  private Long productId;
  private double star;
  private long totalVote;
  private RatingDetail[] voteDetails;

  public ProductRating(Long productId, double star, long[] totalVotes) {
    this.productId = productId;
    this.star = star;
    voteDetails = new RatingDetail[6];
    if (totalVotes != null) {
      this.totalVote = totalVotes[0];
      for (int i = 1; i < 5; i++) {
        voteDetails[i - 1] = new RatingDetail(i, totalVotes[i], (int) (totalVotes[i] / (double) totalVotes[0] * 100));
      }
      voteDetails[4] = new RatingDetail(5, totalVotes[5], 100 - voteDetails[0].getPercent()
          - voteDetails[1].getPercent() - voteDetails[2].getPercent() - voteDetails[3].getPercent());
      this.voteDetails[5] = new RatingDetail(-1, totalVotes[6], 0);
    } else {
      this.totalVote = 0;
      for (int i = 1; i < 7; i++) {
        voteDetails[i - 1] = new RatingDetail(i, 0, 0);
      }
    }
  }
}
