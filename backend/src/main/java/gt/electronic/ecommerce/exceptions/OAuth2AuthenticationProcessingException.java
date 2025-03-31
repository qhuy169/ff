package gt.electronic.ecommerce.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * @author quang huy
 * @created 27/11/2025 - 8:43 PM
 */
public class OAuth2AuthenticationProcessingException extends AuthenticationException {
  public OAuth2AuthenticationProcessingException(String msg, Throwable t) {
    super(msg, t);
  }

  public OAuth2AuthenticationProcessingException(String msg) {
    super(msg);
  }
}