package gt.electronic.ecommerce.models.clazzs;

import gt.electronic.ecommerce.models.enums.ESentiment;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SentimentDetail {
    private int score;
    private long total;
    private double percent;
    private String sentiment;
    public SentimentDetail(ESentiment sentiment, long total) {
        this.score = sentiment.ordinal();
        this.total = total;
        this.sentiment = sentiment.toString();
    }
}
