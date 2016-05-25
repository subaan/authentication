package com.example.util.infrastructure.security;

import com.example.model.Domain;
import com.example.model.User;
import com.example.service.DomainService;
import com.example.service.UserService;
import com.example.util.exceptions.DomainNameNotFoundException;
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

    /** Token service attribute. */
    private TokenService tokenService;
    /** External service authentivator. */
    private ExternalServiceAuthenticator externalServiceAuthenticator;

    @Autowired
    private UserService userService;

    @Autowired
    private DomainService domainService;

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
        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        String domainName = session.getAttribute("domainName").toString();

        User user = new User();

        if(backendAdminUsername.equals(userName)) {
            if(!backendAdminPassword.equals(password.get())) {
                throw new BadCredentialsException("Invalid User Credentials");
            }
        } else {

            if(domainName == null || domainName.equals("")) {
                throw new DomainNameNotFoundException("Domain name not specified");
            } else {

                //Check user under the domain
                domainName = domainName.replace("\"", "");
                Domain domain = domainService.findByAliasName(domainName);
                System.out.println("DB domain is:  "+domain);
                if(domain == null) {
                    throw new DomainNameNotFoundException("Domain with name '" + domainName + "' not found");
                }

                //Check username
                user = userService.findByUsername(userName);
                if(user == null || !user.getUsername().equals(userName)) {
                    throw new UsernameNotFoundException("User with name '" + username.get() + "' not found");
                }

                //Check user under the current domain
                user = userService.findByUsernameAndDomain(userName, domain);
                if(user == null || !user.getUsername().equals(userName)) {
                    throw new UsernameNotFoundException("User with name '" + username.get() + "' not under the domain '"
                    + domain.getAliasName() +"'");
                }

                //Check the credentials
                user = userService.findByUsernameAndPasswordAndDomain(userName, password.get(), domain);
                if(user == null) {
                    throw new BadCredentialsException("Invalid User Credentials !");
                }
            }

        }

        AuthenticationWithToken resultOfAuthentication = externalServiceAuthenticator.authenticate(userName, password.get(), user.getDomain());
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
