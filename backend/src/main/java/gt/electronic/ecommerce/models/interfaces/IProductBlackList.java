package gt.electronic.ecommerce.models.interfaces;

import java.util.Date;

public interface IProductBlackList {
    Long getId();
    Long getProduct_id();
    String getSlug();
    Long getShop_id();
    Date getScan_at();
    double getPercent();
    Long getNeg_total();
    Long getTotal();
    boolean isStatus();
    boolean isBaned();
}
