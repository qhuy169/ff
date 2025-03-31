package gt.electronic.ecommerce.config;

import gt.electronic.ecommerce.dto.response.ResponseObject;
import gt.electronic.ecommerce.filter.JwtTokenFilter;
import gt.electronic.ecommerce.handlers.CustomOAuth2UserService;
import gt.electronic.ecommerce.handlers.HttpCookieOAuth2AuthorizationRequestRepository;
import gt.electronic.ecommerce.handlers.OAuth2AuthenticationFailureHandler;
import gt.electronic.ecommerce.handlers.OAuth2AuthenticationSuccessHandler;
import gt.electronic.ecommerce.utils.MapHelper;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Properties;

/**
 * @author quang huy
 * @created 07/03/2025 - 10:59 PM
 * @project gt-backend
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfiguration {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //// http.cors().and().csrf().disable()
        // // Disable CSRF (cross site request forgery)
        // http.csrf().disable();
        //
        // // No session will be created or used by spring security
        // http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //
        // // Entry points
        // http
        // // Allow everyone to access these addresses
        // .authorizeRequests().antMatchers("/auth/login", "/docs/**",
        //// "/swagger-ui/index.html#/")
        // .permitAll()
        // .anyRequest()
        // .permitAll();
        // // Only admin access these addressses
        //// .antMatchers("/users/**", "/settings/**").hasAuthority("Admin")
        //// .hasAnyAuthority("Admin", "Editor", "Salesperson")
        //// .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
        // // All other requests need to be authenticated to access
        //// .anyRequest().authenticated()
        //// .and()
        //// // Allow user to authenticate with login form
        //// .formLogin().loginPage("/login")
        ////// .usernameParameter("email")
        //// .permitAll()
        //// .and()
        //// .rememberMe().key("AbcdEfghIjklmNopQrsTuvXyz_0123456789")
        //// .and()
        //// // Allow logout
        //// .logout().invalidateHttpSession(true)
        //// .clearAuthentication(true).permitAll();
        //
        // // Exception handling configuration
        // http.exceptionHandling()
        // .accessDeniedHandler(
        // (request, response, ex) -> {
        // response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // response.setStatus(HttpServletResponse.SC_OK);
        //
        // Map<String, Object> map = new HashMap<String, Object>();
        // ResponseObject responseObject =
        // new ResponseObject(HttpStatus.UNAUTHORIZED, ex.getMessage());
        // map = MapHelper.convertObject(responseObject);
        //
        // response.getWriter().write(new JSONObject(map).toString());
        // });
        //
        // http.exceptionHandling()
        // .authenticationEntryPoint(
        // (request, response, ex) -> {
        // response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // response.setStatus(HttpServletResponse.SC_OK);
        //
        // Map<String, Object> map = new HashMap<String, Object>();
        // ResponseObject responseObject =
        // new ResponseObject(HttpStatus.UNAUTHORIZED, ex.getMessage());
        // map = MapHelper.convertObject(responseObject);
        //
        // response.getWriter().write(new JSONObject(map).toString());
        // });
        //
        // // To fix h2-console -
        //// https://stackoverflow.com/questions/53395200/h2-console-is-not-showing-in-browser
        //// http.headers().frameOptions().disable();
        //// http.headers().frameOptions().sameOrigin();
        //
        // // If a user try to access a resource without having enough permissions
        //// http.exceptionHandling().accessDeniedPage("/login");
        //
        //// // Apply JWT
        //// http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
        //
        // // Optional, if you want to test the API from a browser
        // // http.httpBasic();
        //
        // http.addFilterBefore(jwtTokenFilter,
        //// UsernamePasswordAuthenticationFilter.class);
        //
        // return http.build();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/auth/login", "/oauth2/**", "/docs/**", "/swagger-ui/index.html#/")
                .permitAll()
                .anyRequest()
                .permitAll()
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        // Exception handling configuration
        http.exceptionHandling()
                .accessDeniedHandler(
                        (request, response, ex) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(HttpServletResponse.SC_OK);

                            Map<String, Object> map;
                            ResponseObject<?> responseObject = new ResponseObject<>(HttpStatus.UNAUTHORIZED,
                                    ex.getMessage());
                            map = MapHelper.convertObject(responseObject);

                            response.getWriter().write(new JSONObject(map).toString());
                        });

        http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            this.LOGGER.info(ex.toString());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            Map<String, Object> map;
                            ResponseObject<?> responseObject = new ResponseObject<>(HttpStatus.UNAUTHORIZED,
                                    ex.getMessage());
                            map = MapHelper.convertObject(responseObject);

                            response.getWriter().write(new JSONObject(map).toString());
                        });

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    // private static List<String> clients = Arrays.asList("google", "facebook");
    //
    // @Bean
    // public ClientRegistrationRepository clientRegistrationRepository() {
    // List<ClientRegistration> registrations = clients.stream()
    // .map(c -> getRegistration(c))
    // .filter(registration -> registration != null)
    // .collect(Collectors.toList());
    //
    // return new InMemoryClientRegistrationRepository(registrations);
    // }
    //
    // private static String CLIENT_PROPERTY_KEY
    // = "spring.security.oauth2.client.registration.";
    //
    // @Autowired
    // private Environment env;
    //
    // private ClientRegistration getRegistration(String client) {
    // String clientId = env.getProperty(
    // CLIENT_PROPERTY_KEY + client + ".client-id");
    //
    // if (clientId == null) {
    // return null;
    // }
    //
    // String clientSecret = env.getProperty(
    // CLIENT_PROPERTY_KEY + client + ".client-secret");
    //
    // if (client.equals("google")) {
    // return CommonOAuth2Provider.GOOGLE.getBuilder(client)
    // .clientId(clientId).clientSecret(clientSecret).build();
    // }
    // if (client.equals("facebook")) {
    // return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
    // .clientId(clientId).clientSecret(clientSecret).build();
    // }
    // return null;
    // }
    //
    // @Bean
    // public OAuth2AuthorizedClientService authorizedClientService() {
    //
    // return new InMemoryOAuth2AuthorizedClientService(
    // clientRegistrationRepository());
    // }

    // @Bean
    // public WebMvcConfigurer corsConfigurer() {
    // return new WebMvcConfigurerAdapter() {
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    // registry.addMapping("/**");
    //// .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
    // }
    // };
    // }

    // @Bean
    // public WebSecurityCustomizer webSecurityCustomizer() {
    // return (web) -> web.ignoring().antMatchers("/images/**", "/js/**",
    // "/webjars/**");
    // }
}