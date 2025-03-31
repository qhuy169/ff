package gt.electronic.ecommerce.models.interfaces;

import java.math.BigDecimal;

public interface IShopStatistic {
    Long getShop_id();
    Long getOrderCount();
    BigDecimal getEarningTotal();
    BigDecimal getEarningToday();
    BigDecimal getEarningTarget();
    BigDecimal getEarningLastWeek();
    BigDecimal getEarningLastMonth();
}
