package gt.electronic.ecommerce.repositories;

import gt.electronic.ecommerce.entities.Address;
import gt.electronic.ecommerce.entities.keys.AddressKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author quang huy
 * @created 01/03/2025 - 1:00 PM
 * @project gt-backend
 */
@Repository
@Transactional
public interface AddressesRepository extends JpaRepository<Address, AddressKey> {
}
