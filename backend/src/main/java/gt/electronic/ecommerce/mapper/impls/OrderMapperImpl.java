package gt.electronic.ecommerce.mapper.impls;

import com.google.gson.Gson;
import gt.electronic.ecommerce.dto.response.DiscountResponseDTO;
import gt.electronic.ecommerce.dto.response.OrderDetailResponseDTO;
import gt.electronic.ecommerce.dto.response.OrderResponseDTO;
import gt.electronic.ecommerce.entities.Discount;
import gt.electronic.ecommerce.entities.Order;
import gt.electronic.ecommerce.entities.OrderItem;
import gt.electronic.ecommerce.entities.OrderShop;
import gt.electronic.ecommerce.mapper.*;
import gt.electronic.ecommerce.models.clazzs.OrderLog;
import gt.electronic.ecommerce.models.clazzs.OrderPaymentOnly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author quang huy
 * @created 20/09/2025 - 9:34 PM
 * @project gt-backend
 */
@Component
public class OrderMapperImpl implements OrderMapper {
    public static final Gson gson = new Gson();
    private AddressMapper addressMapper;

    @Autowired
    public void AddressMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    private DiscountMapper discountMapper;

    @Autowired
    public void DiscountMapper(DiscountMapper discountMapper) {
        this.discountMapper = discountMapper;
    }

    private OrderItemMapper orderItemMapper;

    @Autowired
    public void OrderItemMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    private UserMapper userMapper;

    @Autowired
    public void UserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public OrderResponseDTO orderToOrderResponseDTO(Order entity, Long shopId, boolean... isFull) {
        if (entity == null) {
            return null;
        }
        OrderShop orderShop = null;
        for (OrderShop shop : entity.getOrderShops()) {
            if (Objects.equals(shop.getShop().getId(), shopId)) {
                orderShop = shop;
                break;
            }
        }
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(entity.getId());
        if (entity.getUser() != null) {
            responseDTO.setUser(this.userMapper.userToUserSimpleResponseDTO(entity.getUser()));
        }
        responseDTO.setGender(entity.getGender().ordinal());
        responseDTO.setFullName(entity.getFullName());
        responseDTO.setTotalPrice(entity.getTotalPrice());
        if (entity.getPayment() != null) {
            responseDTO.setPayment(entity.getPayment().ordinal());
        }
        // if (entity.getShippingMethod() != null) {
        // responseDTO.setShippingMethod(entity.getShippingMethod().getName().ordinal());
        // }
        responseDTO.setTransportFee(entity.getTotalFee());
        responseDTO.setEmail(entity.getEmail());
        responseDTO.setPhone(entity.getPhone());
        if (entity.getDiscounts() != null && entity.getDiscounts().size() > 0) {
            DiscountResponseDTO[] discountDTOs = new DiscountResponseDTO[entity.getDiscounts().size()];
            int i = 0;
            for (Discount discount : entity.getDiscounts()) {
                discountDTOs[i] = new DiscountResponseDTO();
                discountDTOs[i] = this.discountMapper.discountToDiscountResponseDTO(discount);
                i++;
            }
            responseDTO.setDiscounts(discountDTOs);
        }
        if (entity.getLocation() != null) {
            responseDTO.setAddress(
                    this.addressMapper.lineAndLocationToAddressResponseDTO(
                            entity.getLine(), entity.getLocation()));
        }
        responseDTO.setStatus(entity.getStatus().ordinal());
        responseDTO.setPayAt(null);
        responseDTO.setNote(entity.getNote());
        responseDTO.setPaymentOrderCode(entity.getPaymentOrderCode());
        if (isFull.length > 0 && isFull[0]) {
            if (orderShop != null && orderShop.getOrderItems().isEmpty()) {
                OrderDetailResponseDTO[] orderItems = new OrderDetailResponseDTO[orderShop.getOrderItems().size()];
                int i = 0;
                for (OrderItem orderItem : orderShop.getOrderItems()) {
                    if (shopId == null || Objects.equals(orderItem.getProduct().getShop().getId(), shopId)) {
                        orderItems[i] = new OrderDetailResponseDTO();
                        orderItems[i] = this.orderItemMapper.orderItemToOrderDetailResponseDTO(orderItem);
                        i++;
                    }
                }
                responseDTO.setOrderItems(orderItems);
            } else {
                OrderDetailResponseDTO[] orderItems = new OrderDetailResponseDTO[100];
                // List<OrderDetailResponseDTO> orderItems = new ArrayList<>();
                int i = 0;
                for (OrderShop orderShopItem : entity.getOrderShops()) {
                    for (OrderItem orderItem : orderShopItem.getOrderItems()) {
                        if (shopId == null || Objects.equals(orderItem.getProduct().getShop().getId(), shopId)) {
                            // OrderDetailResponseDTO orderItemDTO = new OrderDetailResponseDTO();
                            // orderItemDTO =
                            // this.orderItemMapper.orderItemToOrderDetailResponseDTO(orderItem);
                            // orderItems.add(orderItemDTO);
                            orderItems[i] = new OrderDetailResponseDTO();
                            orderItems[i] = this.orderItemMapper.orderItemToOrderDetailResponseDTO(orderItem);
                            i++;
                        }
                    }
                }
                responseDTO.setOrderItems(Arrays.copyOf(orderItems, i));
            }
        }
        responseDTO.setCreatedAt(entity.getCreatedAt());
        responseDTO.setUpdatedAt(entity.getUpdatedAt());

        return responseDTO;
    }

    @Override
    public OrderResponseDTO orderShopToOrderResponseDTO(OrderShop entity, Long shopId, boolean... isFull) {
        if (entity == null || entity.getOrder() == null) {
            return null;
        }
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(entity.getId());
        if (entity.getOrder().getUser() != null) {
            responseDTO.setUser(this.userMapper.userToUserSimpleResponseDTO(entity.getOrder().getUser()));
        }
        responseDTO.setGender(entity.getOrder().getGender().ordinal());
        responseDTO.setFullName(entity.getOrder().getFullName());
        responseDTO.setTotalPrice(entity.getTotalPrice());
        if (entity.getOrder().getPayment() != null) {
            responseDTO.setPayment(entity.getOrder().getPayment().ordinal());
        }
        // if (entity.getShippingMethod() != null) {
        // responseDTO.setShippingMethod(entity.getShippingMethod().getName().ordinal());
        // }
        responseDTO.setTransportFee(entity.getTotalFee());
        responseDTO.setEmail(entity.getOrder().getEmail());
        responseDTO.setPhone(entity.getOrder().getPhone());
        if (entity.getDiscounts() != null && entity.getDiscounts().size() > 0) {
            DiscountResponseDTO[] discountDTOs = new DiscountResponseDTO[entity.getDiscounts().size()];
            int i = 0;
            for (Discount discount : entity.getDiscounts()) {
                discountDTOs[i] = new DiscountResponseDTO();
                discountDTOs[i] = this.discountMapper.discountToDiscountResponseDTO(discount);
                i++;
            }
            responseDTO.setDiscounts(discountDTOs);
        }
        if (entity.getOrder().getLocation() != null) {
            responseDTO.setAddress(
                    this.addressMapper.lineAndLocationToAddressResponseDTO(
                            entity.getOrder().getLine(), entity.getOrder().getLocation()));
        }
        responseDTO.setStatus(entity.getStatus().ordinal());
        responseDTO.setPayAt(null);
        responseDTO.setNote(entity.getNote());
        if (isFull.length > 0 && isFull[0]) {
            OrderDetailResponseDTO[] orderItems = new OrderDetailResponseDTO[entity.getOrderItems().size()];
            int i = 0;
            for (OrderItem orderItem : entity.getOrderItems()) {
                if (shopId == null || Objects.equals(orderItem.getProduct().getShop().getId(), shopId)) {
                    orderItems[i] = new OrderDetailResponseDTO();
                    orderItems[i] = this.orderItemMapper.orderItemToOrderDetailResponseDTO(orderItem);
                    i++;
                }
            }
            responseDTO.setOrderItems(orderItems);
        }
        responseDTO.setCreatedAt(entity.getCreatedAt());
        responseDTO.setUpdatedAt(entity.getUpdatedAt());
        if (entity.getLog() != null && !entity.getLog().isEmpty()) {
            OrderLog[] orderLogs = gson.fromJson(entity.getLog(), OrderLog[].class);
            responseDTO.setLogs(orderLogs);
        }

        return responseDTO;
    }

    @Override
    public OrderPaymentOnly orderToOrderPaymentOnly(Order entity) {
        if (entity == null) {
            return null;
        }
        return new OrderPaymentOnly(entity.getId(),
                entity.getUser(),
                entity.getPayment(),
                new Date());// entity.getPayAt());
    }
}
