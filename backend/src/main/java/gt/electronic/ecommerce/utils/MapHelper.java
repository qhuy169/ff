package gt.electronic.ecommerce.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @author quang huy
 * @created 08/09/2025 - 8:11 PM
 */
public class MapHelper {
  private MapHelper() {
  }

  private static ObjectMapper mapper = null;

  public static Map<String, Object> convertObject(Object obj) {
    if (mapper == null) {
      mapper = new ObjectMapper();
    }

    return mapper.convertValue(obj, new TypeReference<Map<String, Object>>() {
    });
  }
}
