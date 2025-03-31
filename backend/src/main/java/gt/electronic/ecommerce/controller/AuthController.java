package gt.electronic.ecommerce.controller;

import gt.electronic.ecommerce.dto.request.AuthLoginDTO;
import gt.electronic.ecommerce.dto.request.AuthRegisterDTO;
import gt.electronic.ecommerce.dto.response.AuthResponse;
import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.entities.User;
import gt.electronic.ecommerce.mapper.AuthMapper;
import gt.electronic.ecommerce.services.UserService;
import gt.electronic.ecommerce.utils.JwtTokenUtil;
import gt.electronic.ecommerce.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author quang huy
 * @created 07/03/2025 - 11:13 PM
 * @project gt-backend
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/auth")

public class AuthController {
  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
  @Autowired
  AuthenticationManager authManager;
  private AuthMapper authMapper;

  @Autowired
  public void AuthMapper(AuthMapper authMapper) {
    this.authMapper = authMapper;
  }

  @Autowired
  UserService userService;

  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  public void JwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @PostMapping("/login")
  public ResponseObject<?> login(@RequestBody @Valid AuthLoginDTO request) {
    System.out.println(request.toString());
    if (request.isOtp()) {
      try {
        userService.loadUserByUsername(request.getPhone());
      } catch (UsernameNotFoundException e) {
        this.userService.registerUser(new AuthRegisterDTO(request), false);
      }
      request.setPassword(Utils.DEFAULT_PASSWORD);
    }
    try {
      Authentication authentication = this.authManager.authenticate(
          new UsernamePasswordAuthenticationToken(request.isOtp() ? request.getPhone() : request.getEmail(),
              request.getPassword()));
      User user = (User) authentication.getPrincipal();
      String accessToken = this.jwtTokenUtil.generateAccessToken(authentication);
      // AuthResponse response = new AuthResponse(user.getPhone(), accessToken);
      AuthResponse response = this.authMapper.userToAuthResponse(user, accessToken);

      return new ResponseObject<>(HttpStatus.OK, "Login Successfully", response);

    } catch (BadCredentialsException ex) {
      System.out.println("-1");
      return new ResponseObject<>(HttpStatus.UNAUTHORIZED, "Bad login information");
    }
  }

  @PostMapping("/register")
  public ResponseObject<?> register(
      @RequestBody @Valid AuthRegisterDTO registerDTO,
      @RequestParam(name = "seller", required = false, defaultValue = "false") boolean isSeller) {
    this.userService.registerUser(registerDTO, isSeller);
    try {
      Authentication authentication = this.authManager.authenticate(
          new UsernamePasswordAuthenticationToken(registerDTO.getEmail(), registerDTO.getPassword()));
      String accessToken = jwtTokenUtil.generateAccessToken(authentication);
      User user = (User) authentication.getPrincipal();
      // AuthResponse response = new AuthResponse(user.getPhone(), accessToken);
      AuthResponse response = this.authMapper.userToAuthResponse(user, accessToken);

      return new ResponseObject<>(HttpStatus.OK, "Register Successfully", response);
    } catch (BadCredentialsException ex) {
      return new ResponseObject<>(HttpStatus.UNAUTHORIZED, "Bad login information");
    }
  }

  @PostMapping("/check-access-token")
  public ResponseObject<?> checkToken(HttpServletRequest request) {
    try {
      String accessToken = jwtTokenUtil.getAccessToken(request);
      if (jwtTokenUtil.validateAccessToken(accessToken)) {
        return new ResponseObject<>(HttpStatus.OK, "Access token is valid", null);
      }
    } catch (Exception ex) {
      LOGGER.error(ex.getMessage());
    }
    return new ResponseObject<>(HttpStatus.UNAUTHORIZED, "Access token is non valid", null);
  }

  private static final String authorizationRequestBaseUri = "oauth2/authorization";
  Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

  @Autowired
  private ClientRegistrationRepository clientRegistrationRepository;

  @GetMapping("/login/google")
  public String getLoginPage() {
    Iterable<ClientRegistration> clientRegistrations = null;
    ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
        .as(Iterable.class);
    if (type != ResolvableType.NONE &&
        ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
      clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
    }

    assert clientRegistrations != null;
    clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(),
        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));

    return oauth2AuthenticationUrls.toString();
  }
}
