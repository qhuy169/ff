package gt.electronic.ecommerce.services;

import gt.electronic.ecommerce.entities.Location;

/**
 * @author quang huy
 * @created 26/09/2025 - 3:27 PM
 */
public interface LocationService {
  Location getLocation(Location location);

  Location saveLocation(Location location);
}
