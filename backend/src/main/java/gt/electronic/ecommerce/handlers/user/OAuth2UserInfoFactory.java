package gt.electronic.ecommerce.handlers.user;

import gt.electronic.ecommerce.exceptions.OAuth2AuthenticationProcessingException;
import gt.electronic.ecommerce.models.enums.AuthProvider;

import java.util.Map;

/**
 * @author quang huy
 * @created 27/11/2025 - 9:00 PM
 */
public class OAuth2UserInfoFactory {

  public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
    if (registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
      return new GoogleOAuth2UserInfo(attributes);
    } else if (registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())) {
      return new FacebookOAuth2UserInfo(attributes);
      // } else if (registrationId.equalsIgnoreCase(AuthProvider.github.toString())) {
      // return new GithubOAuth2UserInfo(attributes);
    } else {
      throw new OAuth2AuthenticationProcessingException(
          "Sorry! Login with " + registrationId + " is not supported yet.");
    }
  }
}