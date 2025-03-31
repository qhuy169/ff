package gt.electronic.ecommerce.mapper.impls;

import gt.electronic.ecommerce.dto.response.AddressResponseDTO;
import gt.electronic.ecommerce.entities.Address;
import gt.electronic.ecommerce.entities.Location;
import gt.electronic.ecommerce.mapper.AddressMapper;
import org.springframework.stereotype.Component;

/**
 * @author quang huy
 * @created 20/09/2025 - 10:00 PM
 */
@Component
public class AddressMapperImpl implements AddressMapper {
  @Override
  public AddressResponseDTO lineAndLocationToAddressResponseDTO(String line, Location location) {
    if (location == null) {
      return null;
    }
    AddressResponseDTO responseDTO = new AddressResponseDTO();
    // responseDTO.setLine(line);
    // responseDTO.setCommune(location.getCommune());
    // responseDTO.setDistrict(location.getDistrict());
    // responseDTO.setProvince(location.getProvince());
    responseDTO.setHomeAdd(line);
    responseDTO.setWard(location.getCommune());
    responseDTO.setDistrict(location.getDistrict());
    responseDTO.setCity(location.getProvince());
    return responseDTO;
  }

  @Override
  public AddressResponseDTO addressesToAddressResponseDTO(Address address) {
    if (address == null) {
      return null;
    }
    AddressResponseDTO responseDTO = new AddressResponseDTO();
    responseDTO.setHomeAdd(address.getLine());
    responseDTO.setWard(address.getLocation().getCommune());
    responseDTO.setDistrict(address.getLocation().getDistrict());
    responseDTO.setCity(address.getLocation().getProvince());
    return responseDTO;
  }
}
