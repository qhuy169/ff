package gt.electronic.ecommerce.handlers.user;

import java.util.Map;

/**
 * @author quang huy
 * @created 27/11/2025 - 9:00 PM
 */
public abstract class OAuth2UserInfo {
  protected Map<String, Object> attributes;

  public OAuth2UserInfo(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  public Map<String, Object> getAttributes() {
    return attributes;
  }

  public abstract String getId();

  public abstract String getName();

  public abstract String getEmail();

  public abstract String getImageUrl();
}