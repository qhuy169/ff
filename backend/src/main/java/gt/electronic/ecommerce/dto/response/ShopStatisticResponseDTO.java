package gt.electronic.ecommerce.dto.response;

import gt.electronic.ecommerce.models.clazzs.GroupOrderByDate;
import gt.electronic.ecommerce.models.interfaces.IShopStatistic;
import gt.electronic.ecommerce.utils.Utils;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@ToString
public class ShopStatisticResponseDTO {
    List<GroupOrderByDate> groupOrderByDateList;
    long orderCount;
    BigDecimal earningTotal;
    BigDecimal earningToday;
    BigDecimal earningTarget;
    BigDecimal earningLastWeek;
    BigDecimal earningLastMonth;

    public void setShopStatistic(IShopStatistic shopStatistic) {
        this.orderCount = shopStatistic.getOrderCount();
        this.earningTotal = shopStatistic.getEarningTotal();
        this.earningToday = shopStatistic.getEarningToday();
        this.earningTarget = Utils.getRoundingDigit(shopStatistic.getEarningTarget(), 0, true);
        this.earningLastWeek = shopStatistic.getEarningLastWeek();
        this.earningLastMonth = shopStatistic.getEarningLastMonth();
    }
}
