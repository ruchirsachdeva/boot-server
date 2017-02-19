package com.myapp.service;

import com.myapp.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by rucsac on 18/11/2016.
 */

@Service
public class SocialUserService {
    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;
    @Autowired
    private UsersConnectionRepository usersConnectionRepository;
    @Autowired
    private SocialUserDetailsService socialUserDetailsService;
    @Autowired
    private SecurityContextService securityContextService;


    private static final Logger logger = LoggerFactory.getLogger(SocialUserService.class);



    public  Connection<Facebook> getFacebookConnection(String accessToken) {
        FacebookConnectionFactory connectionFactory = (FacebookConnectionFactory) connectionFactoryLocator.getConnectionFactory("facebook");
        return connectionFactory.createConnection(new AccessGrant(accessToken));
    }

    public  void connectSocial(Connection<?> connection) {
        UserProfile userProfile = connection.fetchUserProfile();
        usersConnectionRepository.createConnectionRepository(userProfile.getEmail()).addConnection(connection);
        logger.debug("User {} {} connection created.", userProfile.getFirstName(), userProfile.getLastName());
    }

    public  void disconnectSocial(Connection<?> connection) {
        UserProfile userProfile = connection.fetchUserProfile();
        usersConnectionRepository.createConnectionRepository(userProfile.getEmail()).removeConnection(connection.getKey());
        logger.debug("User {} {} connection deleted.", userProfile.getFirstName(), userProfile.getLastName());
    }

    public  Optional<User> authenticateSocialUser(Connection<?> connection) {
        SocialAuthenticationToken socialAuthenticationToken = new SocialAuthenticationToken(connection, null);
        Authentication authentication = new SocialAuthenticationProvider(usersConnectionRepository, socialUserDetailsService).authenticate(socialAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserProfile userProfile = connection.fetchUserProfile();
        logger.debug("User {} {} authenticated.", userProfile.getFirstName(), userProfile.getLastName());
        return securityContextService.currentUser();
    }

    public  Optional<User> authenticateSocialUser(String accessToken) {
        return authenticateSocialUser(getFacebookConnection(accessToken));
    }

    public  Optional<User> postUserSignUp(Optional<String> accessToken) {
        return accessToken.map(a -> {
            Connection<?> connection = getFacebookConnection(a);
            connectSocial(connection);
            return authenticateSocialUser(connection).get();
        });
    }
}
