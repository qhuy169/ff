package gt.electronic.ecommerce.mapper.impls;

import gt.electronic.ecommerce.dto.response.ShipmentResponseDTO;
import gt.electronic.ecommerce.entities.OrderShop;
import gt.electronic.ecommerce.entities.Shipment;
import gt.electronic.ecommerce.mapper.AddressMapper;
import gt.electronic.ecommerce.mapper.OrderMapper;
import gt.electronic.ecommerce.mapper.ShipmentMapper;
import gt.electronic.ecommerce.repositories.OrderShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipmentMapperImpl implements ShipmentMapper {
    private AddressMapper addressMapper;

    @Autowired
    public void AddressMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderShopRepository orderShopRepo;

    @Override
    public ShipmentResponseDTO shipmentToShipmentResponseDTO(Shipment entity) {
        if (entity == null) {
            return null;
        }
        ShipmentResponseDTO responseDTO = new ShipmentResponseDTO();
        responseDTO.setId(entity.getId());
        responseDTO.setUserId(entity.getUser().getId());
        OrderShop orderShop = this.orderShopRepo.findById(entity.getOrderShopId()).orElse(null);
        responseDTO.setOrderShop(this.orderMapper.orderShopToOrderResponseDTO(orderShop, null, true));
        if (entity.getFromLocation() != null) {
            responseDTO.setFromAddress(this.addressMapper.lineAndLocationToAddressResponseDTO(entity.getFromLine(),
                                                                                              entity.getFromLocation()));
        }
        if (entity.getToLocation() != null) {
            responseDTO.setToAddress(this.addressMapper.lineAndLocationToAddressResponseDTO(entity.getToLine(),
                                                                                            entity.getToLocation()));
        }
        responseDTO.setTotalPrice(entity.getTotalPrice());
        responseDTO.setStatus(entity.getStatus().toString());
        responseDTO.setCompletedAt(entity.getCompletedAt());
        responseDTO.setCreatedAt(entity.getCreatedAt());
        return responseDTO;
    }
}
