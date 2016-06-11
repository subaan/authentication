package com.example.util.infrastructure.security;

import com.example.model.Domain;
import com.example.model.User;
import com.example.service.DomainService;
import com.example.service.UserService;
import com.example.util.exceptions.DomainNameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.google.common.base.Optional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * Domain username password authentication provider.
 *
 */
public class DomainUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    /** Logger constant. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DomainUsernamePasswordAuthenticationProvider.class);

    /** Token service attribute. */
    private TokenService tokenService;

    /** External service authenticator. */
    private ExternalServiceAuthenticator externalServiceAuthenticator;

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

        LOGGER.info("<------ DB authentication provider --------->");

        @SuppressWarnings("unchecked")
        Optional<String> username = (Optional<String>) authentication.getPrincipal();
        @SuppressWarnings("unchecked")
        Optional<String> password = (Optional<String>) authentication.getCredentials();
        String domainName = authentication.getDetails().toString();

        if (!username.isPresent() || !password.isPresent()) {
            throw new BadCredentialsException("Invalid User Credentials");
        }
        String userName = username.get();
//        ServletRequestAttributes attr = (ServletRequestAttributes)
//                RequestContextHolder.currentRequestAttributes();
//        HttpSession session = attr.getRequest().getSession(true);
//        String domainName = session.getAttribute("domainName").toString();

        AuthenticationWithToken resultOfAuthentication = externalServiceAuthenticator.authenticate(username.get(), password.get(), domainName);
        LOGGER.info("Authenticate : {}", resultOfAuthentication.isAuthenticated());
        if(resultOfAuthentication.isAuthenticated()) {
            LOGGER.info("User authenticate success");
            String newToken = tokenService.generateNewToken();
            resultOfAuthentication.setToken(newToken);
            tokenService.store(newToken, resultOfAuthentication);
        }
        return resultOfAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(DomainUsernamePasswordWithToken.class);
    }
}
