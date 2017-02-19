package com.myapp.controller;

import com.myapp.auth.TokenHandler;
import com.myapp.domain.User;
import com.myapp.service.SecurityContextService;
import com.myapp.service.SocialUserService;
import com.myapp.service.exceptions.UserNotFoundException;
import com.myapp.util.MyControllerAdvice;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author riccardo.causo
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenHandler tokenHandler;
    private final SecurityContextService securityContextService;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private SocialUserService socialUserService;

    @Autowired
    AuthController(AuthenticationManager authenticationManager,
                   TokenHandler tokenHandler,
                   SecurityContextService securityContextService) {
        this.authenticationManager = authenticationManager;
        this.tokenHandler = tokenHandler;
        this.securityContextService = securityContextService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/facebook")
    // TODO : not in use, to be fixed in future when restricting only backend to connect to social and not
    // TODO: expect frontend to send accesstoken.
    // https://www.petrikainulainen.net/programming/spring-framework/adding-social-sign-in-to-a-spring-mvc-web-application-registration-and-login/
    public RedirectView authFacebook(HttpServletResponse response) throws AuthenticationException, IOException {
 //       response.sendRedirect("/auth/facebook?scope=" + MyControllerAdvice.FACEBOOK_SCOPE);

        return new RedirectView("http://localhost:8080/auth/facebook?scope=" + MyControllerAdvice.FACEBOOK_SCOPE, false);


        //return "forward:/auth/facebook?scope=" + MyControllerAdvice.FACEBOOK_SCOPE;
    //    ResponseEntity<String> responseEntity =
      //        restTemplate.exchange("http://localhost:8080/auth/facebook?scope=" + MyControllerAdvice.FACEBOOK_SCOPE, HttpMethod.GET, null, String.class);
      //  Authentication token = SocialAuthenticationToken();
        //authenticationManager.authenticate(token);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/facebook")
    public AuthResponse authFacebook(@RequestBody FacebookAuthParams params) throws UserNotFoundException {
        Optional<User> user = socialUserService.authenticateSocialUser(params.getToken());
        return user.map(u -> {
            final String token = tokenHandler.createTokenForUser(u);
            return new AuthResponse(token);
        }).orElseThrow(RuntimeException::new); // it does not happen.
    }


    @RequestMapping(method = RequestMethod.POST)
    public AuthResponse auth(@RequestBody AuthParams params) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken loginToken = params.toAuthenticationToken();
        final Authentication authentication = authenticationManager.authenticate(loginToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return securityContextService.currentUser().map(u -> {
            final String token = tokenHandler.createTokenForUser(u);
            return new AuthResponse(token);
        }).orElseThrow(RuntimeException::new); // it does not happen.
    }

    @Value
    private static final class AuthParams {
        private final String email;
        private final String password;
        private final String token;


        UsernamePasswordAuthenticationToken toAuthenticationToken() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }

        boolean hasToken() {
            return token!=null;
        }
    }

    @Value
    private static final class FacebookAuthParams {
        private final String token;

    }

    @Value
    private static final class AuthResponse {
        private final String token;
    }

}
