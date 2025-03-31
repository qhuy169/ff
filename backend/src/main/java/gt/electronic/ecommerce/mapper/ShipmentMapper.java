package gt.electronic.ecommerce.mapper;

import gt.electronic.ecommerce.dto.response.ShipmentResponseDTO;
import gt.electronic.ecommerce.entities.Shipment;

public interface ShipmentMapper {
    ShipmentResponseDTO shipmentToShipmentResponseDTO(Shipment entity);
}
