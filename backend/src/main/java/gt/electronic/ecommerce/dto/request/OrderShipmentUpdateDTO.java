package gt.electronic.ecommerce.dto.request;

import gt.electronic.ecommerce.models.enums.EOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
public class OrderShipmentUpdateDTO {
    private EOrderStatus status;
    private String log;
    private Date createdAt;
}
