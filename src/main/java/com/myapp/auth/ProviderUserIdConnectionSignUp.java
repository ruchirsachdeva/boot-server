package com.myapp.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;

/**
 * Extracts the local user-ID from the data given by the provider.
 * Created by rucsac on 15/11/2016.
 */
//@Service
public class ProviderUserIdConnectionSignUp implements ConnectionSignUp
{
    private final Logger LOG =
                  LoggerFactory.getLogger(ProviderUserIdConnectionSignUp.class);


    /**
     24    * This implementation simply reuse the ID, that was provided by the provider.
     25    *
     26    * @param connection
     27    *     The {@link Connection} for the unknown user.
     28    * @return
     29    *     The user-ID, that was provided by the provider.
     30    */
    @Override
    public String execute(Connection connection)
    {
        ConnectionKey key = connection.getKey();
             LOG.info(
                     "signing up user {} from provider {}",
                     key.getProviderUserId(),
                     key.getProviderId()
                     );
             return key.getProviderUserId();
    }
}