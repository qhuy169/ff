package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.dto.request.AuthRegisterDTO;
import gt.electronic.ecommerce.dto.request.UserCreationDTO;
import gt.electronic.ecommerce.dto.response.UserResponseDTO;
import gt.electronic.ecommerce.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author quang huy
 * @created 07/03/2025 - 11:07 PM
 */
public interface UserService extends UserDetailsService {
  Page<UserResponseDTO> getAllUsers(String keyword, Pageable pageable);

  UserResponseDTO getUserById(Long id);

  Long getUserCount();

  UserResponseDTO getUserByAccessToken(String accessToken);

  User getUserByLoginKey(String loginKey);

  UserResponseDTO registerUser(AuthRegisterDTO auth, boolean isSeller);

  UserResponseDTO createUser(UserCreationDTO creationDTO, MultipartFile imageFile);

  UserResponseDTO updateUser(Long id, UserCreationDTO creationDTO, MultipartFile imageFile);

  UserResponseDTO deleteUserById(Long id);

  void saveUser(User user);
}
