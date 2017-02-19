package com.myapp.util;

import com.myapp.auth.TokenHandler;
import com.myapp.domain.User;
import com.myapp.service.SecurityContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.security.SocialAuthenticationProvider;
import org.springframework.social.security.SocialAuthenticationToken;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.Optional;

@Component
public class MyUtil {

	private static final Logger logger = LoggerFactory.getLogger(MyUtil.class);
	
	private static MessageSource messageSource;
	private static TokenHandler tokenHandler;
	private static SecurityContextService securityContextService;

	@Autowired
	public  void setTokenHandler(TokenHandler tokenHandler) {
		MyUtil.tokenHandler = tokenHandler;
	}

	@Autowired
	public  void setSecurityContextService(SecurityContextService securityContextService) {
		MyUtil.securityContextService = securityContextService;
	}

	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		
		MyUtil.messageSource = messageSource;
		
	}

	public static String getMessage(String messageKey, Object... args) {
		return messageSource.getMessage(messageKey, args, Locale.getDefault());
	}


    public static Optional<String> logInUser(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
		return securityContextService.currentUser().map(u -> {
			final String token = tokenHandler.createTokenForUser(u);
			return token;
		});
    }
    
    public static boolean isAuthorized(Facebook facebook) {
    	try {
    		return facebook.isAuthorized();
    	} catch (Throwable t){
    		return false;
    	}
    }

	public static void authenticate(Connection<?> connection) {
		UserProfile userProfile = connection.fetchUserProfile();
		String username = userProfile.getUsername();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		logger.debug("User {} {} connected.", userProfile.getFirstName(), userProfile.getLastName());
	}

}
