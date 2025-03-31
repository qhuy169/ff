package gt.electronic.ecommerce.services.impls;

import gt.electronic.ecommerce.entities.Address;
import gt.electronic.ecommerce.repositories.AddressesRepository;
import gt.electronic.ecommerce.services.AddressService;
import gt.electronic.ecommerce.services.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author quang huy
 * @created 01/03/2025 - 1:02 PM
 */
@Service
@Transactional
public class AddressServiceImpl implements AddressService {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  public static final String branchName = Address.class.getSimpleName();

  private AddressesRepository addressesRepo;

  @Autowired
  public void AddressesRepository(AddressesRepository addressesRepo) {
    this.addressesRepo = addressesRepo;
  }

  private LocationService locationService;

  @Autowired
  public void LocationService(LocationService locationService) {
    this.locationService = locationService;
  }

  @Override
  public Address saveAddress(Address address) {
    return this.addressesRepo.findById(address.getId()).orElseGet(() -> this.addressesRepo.save(address));
  }
}
