package gt.electronic.ecommerce.entities.keys;

import gt.electronic.ecommerce.entities.Order;
import gt.electronic.ecommerce.entities.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author quang huy
 * @created 03/12/2025 - 8:31 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderShopKey implements Serializable {
  @Column(name = "product_id")
  Long orderId;

  @Column(name = "shop_id")
  Long shopId;

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof OrderShopKey))
      return false;
    OrderShopKey that = (OrderShopKey) o;
    return this.getOrderId().equals(that.getOrderId()) && this.getShopId().equals(that.getShopId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getOrderId(), this.getShopId());
  }

  public OrderShopKey(Order order, Shop shop) {
    this.orderId = order.getId();
    this.shopId = shop.getId();
  }
}
