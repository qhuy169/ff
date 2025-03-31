package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author quang huy
 * @created 10/09/2025 - 12:16 PM
 * @project gt-backend
 */
@Repository
@Transactional
public interface ImageRepository extends JpaRepository<Image, Long> {
}
