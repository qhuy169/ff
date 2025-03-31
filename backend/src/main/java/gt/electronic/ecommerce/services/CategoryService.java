package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.dto.request.CategoryCreationDTO;
import gt.electronic.ecommerce.dto.response.CategoryResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author quang huy
 * @created 11/09/2025 - 10:01 AM
 */
public interface CategoryService {
  List<CategoryResponseDTO> getAllCategories(String keyword);

  List<CategoryResponseDTO> getAllCategoriesByShop(Long shopId);

  CategoryResponseDTO getCategoryById(Integer id);

  CategoryResponseDTO getCategoryBySlug(String slug);

  CategoryResponseDTO createCategory(CategoryCreationDTO creationDTO, MultipartFile imageFile);

  CategoryResponseDTO updateCategory(Integer id, CategoryCreationDTO creationDTO, MultipartFile imageFile);

  CategoryResponseDTO deleteCategoryById(Integer id);
}
