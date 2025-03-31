package gt.electronic.ecommerce.mapper;

import gt.electronic.ecommerce.dto.response.AuthResponse;
import gt.electronic.ecommerce.entities.User;

/**
 * @author quang huy
 * @created 01/03/2025 - 12:11 PM
 * @project gt-backend
 */
public interface AuthMapper {
  AuthResponse userToAuthResponse(User entity, String accessToken);
}
