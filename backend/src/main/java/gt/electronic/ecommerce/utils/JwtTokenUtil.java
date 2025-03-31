package gt.electronic.ecommerce.utils;

import gt.electronic.ecommerce.entities.User;
import gt.electronic.ecommerce.exceptions.AccessTokenNotValidException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author quang huy
 * @created 08/09/2025 - 5:45 PM
 */
@Component
public class JwtTokenUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

  @Value("${app.auth.tokenExpirationMs}")
  private long EXPIRE_DURATION;

  // @Value("${app.auth.tokenRefreshExpirationDateMsec}")
  // private long REFRESH_EXPIRATION;

  @Value("${app.auth.tokenSecret}")
  private String SECRET_KEY;

  public String generateAccessToken(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return Jwts.builder()
        .setSubject(String.format("%s,%s", user.getId(),
            user.getPhone() != null ? user.getPhone() : user.getEmail()))
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
        .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
        .compact();

  }

  // used to verify a given JWT. It returns true if the JWT is verified, or false
  // otherwise.
  public boolean validateAccessToken(String token) {
    try {
      Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
      return true;
    } catch (SignatureException e) {
      LOGGER.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      LOGGER.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      LOGGER.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      LOGGER.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      LOGGER.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

  // gets the value of the subject field of a given token.
  // The subject contains User ID and email, which will be used to recreate a User
  // object.
  public String getSubject(String token) {
    return parseClaims(token).getSubject();
  }

  private Claims parseClaims(String token) {
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
  }

  // public String getUserNameFromJwtToken(String token) {
  // return
  // Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
  // }

  public String getAccessToken(HttpServletRequest request) {
    String token = null;
    try {
      String header = request.getHeader("Authorization");
      if (header == null)
        return null;
      token = header.split(" ")[1].trim();
    } catch (ArrayIndexOutOfBoundsException ex) {
      LOGGER.error("Bearer is null" + ex.getMessage());
    }
    return token;
  }

  public String getUserNameFromJwtToken(String accessToken) {
    if (this.validateAccessToken(accessToken)) {
      return this.getSubject(accessToken).split(",")[1];
    }
    throw new AccessTokenNotValidException();
  }

  public String getUserNameFromRequest(HttpServletRequest request) {
    try {
      if (getAccessToken(request) != null) {
        return this.getSubject(getAccessToken(request)).split(",")[1];
      } else {
        return null;
      }
    } catch (Exception e) {
      return null;
    }
  }

  // public String setExpiredJwtToken(String token) {
  // return
  // Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().setExpiration(new
  // Date())
  // .getSubject();
  // }

  // public String refreshToken(String token) {
  // final Date createdDate = new Date();
  // final Date expirationDate = new Date(createdDate.getTime() +
  // REFRESH_EXPIRATION);
  //
  // final Claims claims = parseClaims(token);
  // claims.setIssuedAt(createdDate);
  // claims.setExpiration(new Date());
  //
  // return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512,
  // SECRET_KEY).compact();
  // }
}
