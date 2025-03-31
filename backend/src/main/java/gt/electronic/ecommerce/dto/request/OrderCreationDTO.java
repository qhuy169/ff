package gt.electronic.ecommerce.dto.request;

import gt.electronic.ecommerce.entities.*;
import gt.electronic.ecommerce.models.enums.EGender;
import gt.electronic.ecommerce.models.enums.EOrderStatus;
import gt.electronic.ecommerce.models.enums.EPaymentType;
import gt.electronic.ecommerce.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.*;

/**
 * @author quang huy
 * @created 20/09/2025 - 9:13 PM
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationDTO {
  private Long userId;

  // @NotBlank(message = "Gender not blank!")
  // @NotNull(message = "Gender not null!")
  private EGender gender;

  @NotBlank(message = "FullName not blank!")
  @NotNull(message = "FullNane not null!")
  private String fullName;

  // @NotBlank(message = "Email not blank!")
  private String email;

  @NotBlank(message = "Phone not blank!")
  @NotNull(message = "Phone not null!")
  private String phone;

  // @NotBlank(message = "Address not blank!")
  @NotNull(message = "Address not null!")
  private AddressCreationDTO address;
  private EPaymentType payment;
  // private EShippingMethod shippingMethod;
  // private Date expectedDeliveryTime;
  // private BigDecimal transportFee;
  private Long[] discountIds;
  private String note;
  private EOrderStatus status;

  // @NotBlank(message = "OrderItems not blank!")
  private List<OrderShopCreationDTO> orderShops;

  public OrderCreationDTO(User user, Map<Product, Long> productMap) {
    this.userId = user.getId();
    this.gender = user.getGender();
    this.fullName = Utils.getFullNameFromLastNameAndFirstName(user.getLastName(), user.getFirstName());
    this.email = user.getEmail();
    this.phone = user.getPhone();
    this.address = new AddressCreationDTO(user.getAddresses());
    this.payment = EPaymentType.CASH;
    // this.shippingMethod = EShippingMethod.GHN_EXPRESS;
    // this.orderItems = orderItems;
    this.status = EOrderStatus.ORDER_SHIPPING;
    Map<Shop, List<OrderDetailCreationDTO>> groupProductByShop = new HashMap<>();
    for (Map.Entry<Product, Long> entry : productMap.entrySet()) {
      OrderDetailCreationDTO detail = new OrderDetailCreationDTO(entry.getKey(), entry.getValue());
      List<OrderDetailCreationDTO> details = groupProductByShop.get(entry.getKey().getShop());
      if (details == null) {
        groupProductByShop.put(entry.getKey().getShop(), new ArrayList<>(Collections.singleton(detail)));
      } else {
        details.add(detail);
        groupProductByShop.put(entry.getKey().getShop(), details);
      }
    }
    orderShops = new ArrayList<>();
    for (Map.Entry<Shop, List<OrderDetailCreationDTO>> entry : groupProductByShop.entrySet()) {
      OrderShopCreationDTO shop = new OrderShopCreationDTO(entry.getKey(), entry.getValue());
      this.orderShops.add(shop);
    }
  }
}
