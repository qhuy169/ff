package gt.electronic.ecommerce.models.clazzs;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopSentiment {
    private Long shopId;
    ProductSentiment[] productSentiments;
}
