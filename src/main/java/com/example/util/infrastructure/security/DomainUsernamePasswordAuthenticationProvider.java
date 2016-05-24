package com.example.util.infrastructure.security;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.google.common.base.Optional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Domain username password authentication provider.
 *
 */
public class DomainUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    /** Token service attribute. */
    private TokenService tokenService;
    /** External service authentivator. */
    private ExternalServiceAuthenticator externalServiceAuthenticator;

    @Autowired
    private UserService userService;

    /** Admin username. */
    @Value("${backend.admin.username}")
    private String backendAdminUsername;

    /** Admin password. */
    @Value("${backend.admin.password}")
    private String backendAdminPassword;

    /**
     * Parameterized constructor.
     * @param tokenService to set
     * @param externalServiceAuthenticator to set
     */
    public DomainUsernamePasswordAuthenticationProvider(TokenService tokenService, ExternalServiceAuthenticator externalServiceAuthenticator) {
        this.tokenService = tokenService;
        this.externalServiceAuthenticator = externalServiceAuthenticator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        @SuppressWarnings("unchecked")
        Optional<String> username = (Optional<String>) authentication.getPrincipal();
        @SuppressWarnings("unchecked")
        Optional<String> password = (Optional<String>) authentication.getCredentials();

        if (!username.isPresent() || !password.isPresent()) {
            throw new BadCredentialsException("Invalid User Credentials");
        }
        String userName = username.get();

        if(backendAdminUsername.equals(userName)) {
            if(!backendAdminPassword.equals(password.get())) {
                throw new BadCredentialsException("Invalid User Credentials");
            }
        } else {
            User user = userService.findByUsername(userName);
            if(user == null || !user.getUsername().equals(userName)) {
                throw new UsernameNotFoundException("Username '" + username.get() + "' not found");
            }

            user = userService.findByUsernameAndPassword(userName, password.get());
            if(user == null) {
                throw new BadCredentialsException("Invalid User Credentials");
            }
        }

        AuthenticationWithToken resultOfAuthentication = externalServiceAuthenticator.authenticate(userName, password.get());
        String newToken = tokenService.generateNewToken();
        resultOfAuthentication.setToken(newToken);
        tokenService.store(newToken, resultOfAuthentication);

        return resultOfAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
