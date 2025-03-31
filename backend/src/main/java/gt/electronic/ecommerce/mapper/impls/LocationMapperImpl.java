package gt.electronic.ecommerce.mapper.impls;

import gt.electronic.ecommerce.dto.request.AddressCreationDTO;
import gt.electronic.ecommerce.entities.Location;
import gt.electronic.ecommerce.mapper.LocationMapper;
import org.springframework.stereotype.Component;

/**
 * @author quang huy
 * @created 08/10/2025 - 8:09 PM
 * @project gt-backend
 */
@Component
public class LocationMapperImpl implements LocationMapper {
  @Override
  public Location AddressCreationDTOToLocation(AddressCreationDTO creationDTO) {
    if (creationDTO == null) {
      return null;
    }
    Location entity = new Location();
    entity.setCommune(creationDTO.getWard());
    entity.setDistrict(creationDTO.getDistrict());
    entity.setProvince(creationDTO.getCity());

    return entity;
  }
}
