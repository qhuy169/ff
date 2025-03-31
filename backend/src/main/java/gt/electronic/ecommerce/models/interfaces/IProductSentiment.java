package gt.electronic.ecommerce.models.interfaces;

import gt.electronic.ecommerce.models.enums.ESentiment;

public interface IProductSentiment {
    Long getProduct_id();
    Long getShop_id();
    ESentiment getSentiment();
    Long getCount();

}
