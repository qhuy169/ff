package gt.electronic.ecommerce.mapper.impls;

import gt.electronic.ecommerce.dto.response.UserResponseDTO;
import gt.electronic.ecommerce.dto.response.UserSimpleResponseDTO;
import gt.electronic.ecommerce.entities.Address;
import gt.electronic.ecommerce.entities.User;
import gt.electronic.ecommerce.mapper.AddressMapper;
import gt.electronic.ecommerce.mapper.UserMapper;
import gt.electronic.ecommerce.models.enums.EGender;
import gt.electronic.ecommerce.models.enums.ERole;
import gt.electronic.ecommerce.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author quang huy
 * @created 10/09/2025 - 3:38 PM
 */
@Component
public class UserMapperImpl implements UserMapper {
  private AddressMapper addressMapper;

  @Autowired
  public void AddressMapper(AddressMapper addressMapper) {
    this.addressMapper = addressMapper;
  }

  @Override
  public UserResponseDTO userToUserResponseDTO(User entity) {
    if (entity == null) {
      return null;
    }
    UserResponseDTO responseDTO = new UserResponseDTO();
    responseDTO.setId(entity.getId());
    responseDTO.setUsername(entity.getUsername());
    // responseDTO.setChangedUsername(entity.isChangedUsername());
    responseDTO.setFullName(Utils.getFullNameFromLastNameAndFirstName(entity.getLastName(), entity.getFirstName()));
    responseDTO.setFirstName(entity.getFirstName());
    responseDTO.setLastName(entity.getLastName());
    responseDTO.setEmail(entity.getEmail());
    responseDTO.setEmailVerified(entity.isEmailVerified());
    responseDTO.setPhone(entity.getPhone());
    responseDTO.setPhoneVerified(entity.isPhoneVerified());
    responseDTO.setIdentityCard(entity.getIdentityCard());
    responseDTO.setBirthDate(entity.getBirthDate());
    responseDTO.setGender(entity.getGender() != null ? entity.getGender().ordinal() : EGender.UNKNOWN.ordinal());

    if (entity.getAddresses() != null && entity.getAddresses().size() > 0) {
      Address defaultAddress = null;
      for (Address address : entity.getAddresses()) {
        if (defaultAddress == null || address.isDefault()) {
          defaultAddress = address;
        }
      }
      responseDTO.setAddress(addressMapper.addressesToAddressResponseDTO(defaultAddress));
    }
    if (entity.getShop() != null) {
      responseDTO.setShopId(entity.getShop().getId());
    }

    responseDTO.setEnabled(entity.isEnabled());
    if (entity.getAvatar() != null) {
      responseDTO.setAvatar(Utils.getUrlFromPathImage(entity.getAvatar().getPath()));
    }
    // int roleSize = entity.getRoles().size();
    // int[] roles = new ERole[roleSize];
    // int i = 0;
    // for (Role role : entity.getRoles()) {
    // roles[i] = role.getName();
    // i++;
    // }
    // responseDTO.setRoles(roles);
    responseDTO.setRole(entity.getRole().ordinal());
    responseDTO.setCreatedAt(entity.getCreatedAt());
    responseDTO.setUpdatedAt(entity.getUpdatedAt());
    responseDTO.setLastLogin(entity.getLastLogin());
    return responseDTO;
  }

  @Override
  public UserSimpleResponseDTO userToUserSimpleResponseDTO(User entity) {
    if (entity == null) {
      return null;
    }
    UserSimpleResponseDTO responseDTO = new UserSimpleResponseDTO();
    responseDTO.setId(entity.getId());
    responseDTO.setUsername(entity.getUsername());
    responseDTO.setFullName(Utils.getFullNameFromLastNameAndFirstName(entity.getLastName(), entity.getFirstName()));
    responseDTO.setEmail(entity.getEmail());
    responseDTO.setPhone(entity.getPhone());
    if (entity.getAvatar() != null) {
      responseDTO.setAvatar(Utils.getUrlFromPathImage(entity.getAvatar().getPath()));
    }
    responseDTO.setAdmin(entity.getRole() == ERole.ROLE_ADMIN);
    return responseDTO;
  }
}
