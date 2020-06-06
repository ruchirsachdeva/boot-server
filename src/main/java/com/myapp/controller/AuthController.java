package com.myapp.controller;

import com.myapp.auth.TokenHandler;
import com.myapp.service.SecurityContextService;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * @author riccardo.causo
 */
@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com"})
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenHandler tokenHandler;
    private final SecurityContextService securityContextService;

    @Autowired
    AuthController(AuthenticationManager authenticationManager,
                   TokenHandler tokenHandler,
                   SecurityContextService securityContextService) {
        this.authenticationManager = authenticationManager;
        this.tokenHandler = tokenHandler;
        this.securityContextService = securityContextService;
    }


    @CrossOrigin(origins = {"http://localhost:4200", "https://party-client-app.herokuapp.com"})
    @RequestMapping(method = RequestMethod.POST)
    public AuthParams.AuthResponse auth(@RequestBody AuthParams params) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken loginToken = params.toAuthenticationToken();
        final Authentication authentication = authenticationManager.authenticate(loginToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return securityContextService.currentUser().map(u -> {
            final String token = tokenHandler.createTokenForUser(u);
            return new AuthParams.AuthResponse(token);
        }).orElseThrow(RuntimeException::new); // it does not happen.
    }

    @Value
    private static final class AuthParams {
        private final String email;
        private final String password;

        UsernamePasswordAuthenticationToken toAuthenticationToken() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }

        @Value
        private static final class AuthResponse {
            private final String token;
        }

    }
}
