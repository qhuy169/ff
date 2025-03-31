package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.dto.response.DescriptionResponseDTO;
import gt.electronic.ecommerce.entities.Description;

import java.util.List;

/**
 * @author quang huy
 * @created 11/09/2025 - 8:18 PM
 */
public interface DescriptionService {
  List<DescriptionResponseDTO> getAllDescriptions();

  DescriptionResponseDTO getDescriptionById(Long id);

  Description createDescription(Description description);

  Description updateDescription(Long id, Description description);

  Description deleteDescriptionById(Long id);
}
