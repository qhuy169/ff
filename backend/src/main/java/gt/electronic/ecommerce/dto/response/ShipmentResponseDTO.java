package gt.electronic.ecommerce.dto.response;

import gt.electronic.ecommerce.models.enums.EShipmentStatus;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
public class ShipmentResponseDTO {
    private String id;
    private Long userId;
    private OrderResponseDTO orderShop;
    private AddressResponseDTO fromAddress;
    private AddressResponseDTO toAddress;
    private BigDecimal totalPrice;
    private String status;
    private Date createdAt;
    private Date completedAt;
}
