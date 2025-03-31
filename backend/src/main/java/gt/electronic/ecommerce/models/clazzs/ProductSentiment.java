package gt.electronic.ecommerce.models.clazzs;

import com.fasterxml.jackson.databind.DatabindException;
import gt.electronic.ecommerce.models.enums.ESentiment;
import gt.electronic.ecommerce.utils.Utils;
import lombok.*;
import org.springframework.security.core.parameters.P;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductSentiment {
    private Long productId;
    private Double avgScore;
    private Date date;
    private String sentiment;
    private long totalSentiment = 0;
    private SentimentDetail[] sentimentDetails = new SentimentDetail[ESentiment.values().length - 1];

    public ProductSentiment(Long productId, long[] totalSentiments) {
        this.productId = productId;
        int totalSentiment = ESentiment.values().length - 1;
        double totalScoreElement = 0;
        if (totalSentiments != null) {
            this.totalSentiment = totalSentiments[0];
            int percent = 0;
            for (int i = 0; i < totalSentiment; i++) {
                sentimentDetails[i] =
                        new SentimentDetail(i, totalSentiments[i + 1],
                                            (int) (totalSentiments[i + 1] / (double) totalSentiments[0] * 100),
                                            ESentiment.values()[i].toString());
                if (sentimentDetails[i].getTotal() != 0) {
                    totalScoreElement += sentimentDetails[i].getTotal() * sentimentDetails[i].getScore();
                }
                if (i != totalSentiment - 1) {
                    percent += sentimentDetails[i].getPercent();
                }
            }
            sentimentDetails[totalSentiment - 1].setPercent(100 - percent);
            this.sentiment = getSentimentByAvgScore(totalScoreElement / (double) this.totalSentiment);
        } else {
            this.totalSentiment = 0;
            for (int i = 0; i < totalSentiment; i++) {
                sentimentDetails[i] = new SentimentDetail(i, 0, 0, ESentiment.values()[i].toString());
            }
            this.sentiment = ESentiment.SENTIMENT_UNKNOWN.toString();
        }
    }

    public ProductSentiment(Long productId) {
        this.productId = productId;
    }

    public void addSentimentDetail(SentimentDetail sentimentDetail) {
        this.sentimentDetails[sentimentDetail.getScore()] = sentimentDetail;
        this.totalSentiment += sentimentDetail.getTotal();
    }

    public ProductSentiment updateProductSentiment() {
        int percent = 0;
        double totalScoreElement = 0;
        for (int i = 0; i < sentimentDetails.length; i++) {
            if (sentimentDetails[i] == null) {
                sentimentDetails[i] =
                        new SentimentDetail(ESentiment.values()[i].ordinal(), 0, 0, ESentiment.values()[i].toString());
            } else if (i != sentimentDetails.length - 1) {
                sentimentDetails[i].setPercent(
                        (int) (sentimentDetails[i].getTotal() / (double) this.totalSentiment * 100));
                percent += sentimentDetails[i].getPercent();
            }
            if (sentimentDetails[i].getTotal() != 0) {
                totalScoreElement += sentimentDetails[i].getTotal() * sentimentDetails[i].getScore();
            }
        }
        sentimentDetails[sentimentDetails.length - 1].setPercent(100 - percent);
        if (this.totalSentiment != 0) {
            this.sentiment = getSentimentByAvgScore(totalScoreElement / (double) this.totalSentiment);
        } else {
            this.sentiment = ESentiment.SENTIMENT_UNKNOWN.toString();
        }
        return this;
    }

    public String getSentimentByAvgScore(double avgScore) {
        this.avgScore = Utils.getRoundingDigit(avgScore, 2);
        if (avgScore >= 1.3) {
            return ESentiment.SENTIMENT_POSITIVE.toString();
        } else if (avgScore <= 0.66) {
            return ESentiment.SENTIMENT_NEGATIVE.toString();
        } else {
            return ESentiment.SENTIMENT_NEUTRAL.toString();
        }
    }
}
