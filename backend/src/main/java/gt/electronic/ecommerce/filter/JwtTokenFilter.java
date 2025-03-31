package gt.electronic.ecommerce.filter;

import gt.electronic.ecommerce.entities.User;
import gt.electronic.ecommerce.services.UserService;
import gt.electronic.ecommerce.utils.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author quang huy
 * @created 08/09/2025 - 5:44 PM
 * @project gt-backend
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtTokenUtil jwtUtil;

  @Autowired
  private UserService userService;

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

  // This is to be replaced with a list of domains allowed to access the server
  // You can include more than one origin here
  // private final List<String> allowedOrigins =
  // Arrays.asList("http://127.0.0.1:5173", "http://127.0.0.1:5000");

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    response.setHeader("Access-control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
    response.setHeader("Access-Control-Allow-Headers",
        "Authorization, Content-Type, Accept, X-Requested-With, remember-me, x-auth-token");
    response.setHeader("Access-Control-Max-Age", "3600");
    response.setHeader("Access-Control-Allow-Credentials", "true");

    // // Lets make sure that we are working with HTTP (that is, against
    // HttpServletRequest and HttpServletResponse objects)
    // if (request instanceof HttpServletRequest && response instanceof
    // HttpServletResponse) {
    // HttpServletRequest req = (HttpServletRequest) request;
    // HttpServletResponse res = (HttpServletResponse) response;
    //
    // // Access-Control-Allow-Origin
    // String origin = request.getHeader("Origin");
    // res.setHeader("Vary", "Origin");
    //
    // // Access-Control-Max-Age
    // res.setHeader("Access-Control-Max-Age", "3600");
    //
    // // Access-Control-Allow-Credentials
    // res.setHeader("Access-Control-Allow-Credentials", "true");
    //
    // // Access-Control-Allow-Methods
    // res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    //
    // // Access-Control-Allow-Headers
    // res.setHeader("Access-Control-Allow-Headers",
    // "Origin, X-Requested-With, Content-Type, Accept, " + "X-CSRF-TOKEN");
    // }

    // If the Authorization header of the request doesn’t contain a Bearer token,
    // it continues the filter chain without updating authentication context.
    if (!hasAuthorizationBearer(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    // Else, if the token is not verified, continue the filter chain without
    // updating authentication context.
    String token = jwtUtil.getAccessToken(request);
    if (!jwtUtil.validateAccessToken(token)) {
      filterChain.doFilter(request, response);
      return;
    }

    // If the token is verified, update the authentication context with the user
    // details ID and phone.
    // In other words, it tells Spring that the user is authenticated, and continue
    // the downstream filters.
    setAuthenticationContext(token, request);
    // if (request.getServletPath().equals("/auth/logout")) {
    // LOGGER.info("Logout");
    // String message = jwtUtil.setExpiredJwtToken(token);
    // }/*
    filterChain.doFilter(request, response);
  }

  private boolean hasAuthorizationBearer(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
      return false;
    }

    return true;
  }

  private void setAuthenticationContext(String token, HttpServletRequest request) {
    UserDetails userDetails = getUserDetails(token);

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
        userDetails.getAuthorities());

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private UserDetails getUserDetails(String token) {
    User userDetails = new User();
    String[] jwtSubject = jwtUtil.getSubject(token).split(",");

    userDetails = (User) userService.loadUserByUsername(jwtSubject[1]);

    return (UserDetails) userDetails;
  }
}
