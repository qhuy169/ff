package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.dto.response.DescriptionResponseDTO;
import gt.electronic.ecommerce.entities.Description;
import gt.electronic.ecommerce.repositories.DescriptionRepository;
import gt.electronic.ecommerce.services.DescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author quang huy
 * @created 11/09/2025 - 8:21 PM
 */
@Service
@Transactional
public class DescriptionServiceImpl implements DescriptionService {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = Description.class.getSimpleName();
  private DescriptionRepository descriptionRepo;

  @Autowired
  public void DescriptionRepository(DescriptionRepository descriptionRepo) {
    this.descriptionRepo = descriptionRepo;
  }

  @Override
  public List<DescriptionResponseDTO> getAllDescriptions() {
    return null;
  }

  @Override
  public DescriptionResponseDTO getDescriptionById(Long id) {
    return null;
  }

  @Override
  public Description createDescription(Description description) {
    Description saved;
    if (description.getCategories() == null) {
      saved = this.descriptionRepo.findByName(description.getName()).orElse(null);
    } else {
      saved = this.descriptionRepo.findByNameAndCategories(
          description.getName(),
          description.getCategories().stream().findFirst().orElse(null)).orElse(null);
    }
    if (saved != null) {
      return saved;
    }
    return this.descriptionRepo.save(description);
  }

  @Override
  public Description updateDescription(Long id, Description description) {
    return null;
  }

  @Override
  public Description deleteDescriptionById(Long id) {
    return null;
  }
}
