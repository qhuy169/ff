package gt.electronic.ecommerce.mapper;

import gt.electronic.ecommerce.dto.request.UserCreationDTO;
import gt.electronic.ecommerce.dto.response.UserResponseDTO;
import gt.electronic.ecommerce.dto.response.UserSimpleResponseDTO;
import gt.electronic.ecommerce.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author quang huy
 * @created 10/09/2025 - 11:52 AM
 * @project gt-backend
 */
public interface UserMapper {
  UserResponseDTO userToUserResponseDTO(User entity);

  UserSimpleResponseDTO userToUserSimpleResponseDTO(User entity);
}
