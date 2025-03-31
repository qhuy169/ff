package gt.electronic.ecommerce.mapper;

import gt.electronic.ecommerce.dto.response.CategoryResponseDTO;
import gt.electronic.ecommerce.dto.response.UserResponseDTO;
import gt.electronic.ecommerce.entities.Category;
import gt.electronic.ecommerce.entities.User;

/**
 * @author quang huy
 * @created 11/09/2025 - 12:37 PM
 * @project gt-backend
 */
public interface CategoryMapper {
  CategoryResponseDTO categoryToCategoryResponseDTO(Category entity);
}
