package gt.electronic.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author quang huy
 * @created 01/03/2025 - 10:09 PM
 * @project gt-backend
 */
@RestController
public class WelcomeController {
  @GetMapping("/")
  public String hello() {
    return "welcome to website";
  }
}
