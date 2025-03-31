package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.dto.request.ShopCreationDTO;
import gt.electronic.ecommerce.dto.request.UserCreationDTO;
import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.dto.response.ShopResponseDTO;
import gt.electronic.ecommerce.dto.response.UserResponseDTO;
import gt.electronic.ecommerce.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:01 AM
 */
public interface ShopService {
  List<ShopResponseDTO> getAllShops(String keyword);

  ShopResponseDTO getShopById(Long id);

  ShopResponseDTO getShopBySlug(String slug);

  ShopResponseDTO getShopByUser(Long userId);

  ShopResponseDTO getShopByAccessToken(String accessToken);

  ShopResponseDTO registerShop(User user);

  ShopResponseDTO createShop(String loginKey, ShopCreationDTO creationDTO, MultipartFile avatarFile,
      MultipartFile backgroundFile, boolean... isAdmin);

  ShopResponseDTO updateShop(String loginKey, Long id, ShopCreationDTO creationDTO, MultipartFile avatarFile,
      MultipartFile backgroundFile, boolean... isAdmin);

  ShopResponseDTO deleteShopById(String loginKey, Long id, boolean... isAdmin);
}
