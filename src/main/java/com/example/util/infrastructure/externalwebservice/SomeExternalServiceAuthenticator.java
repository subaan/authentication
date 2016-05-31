package com.example.util.infrastructure.externalwebservice;

import com.example.model.Domain;
import com.example.service.DomainService;
import com.example.service.UserService;
import com.example.util.exceptions.DomainNameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;

import com.example.model.User;
import com.example.util.infrastructure.AuthenticatedExternalWebService;
import com.example.util.infrastructure.security.AuthenticationWithToken;
import com.example.util.infrastructure.security.ExternalServiceAuthenticator;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * External service authenticator.
 *
 */
public class SomeExternalServiceAuthenticator implements ExternalServiceAuthenticator {

    @Autowired
    private DomainService domainService;

    @Autowired
    private UserService userService;

    /** Admin username. */
    @Value("${backend.admin.username}")
    private String backendAdminUsername;

    /** Admin password. */
    @Value("${backend.admin.password}")
    private String backendAdminPassword;

    @Override
    public AuthenticationWithToken authenticate(String username, String password, String domainName) {
        ExternalWebServiceStub externalWebService = new ExternalWebServiceStub();


        User user = new User();

        if(backendAdminUsername.equals(username)) {
            if(!backendAdminPassword.equals(password)) {
                throw new BadCredentialsException("Invalid User Credentials");
            }
        } else {

            if(domainName == null || domainName.equals("")) {
                throw new DomainNameNotFoundException("Domain name not specified");
            } else {

                //Check user under the domain
//                domainName = domainName.replace("\"", "");
                Domain domain = domainService.findByAliasName(domainName);
                System.out.println("DB domain is:  "+domain);
                if(domain == null) {
                    throw new DomainNameNotFoundException("Domain with name '" + domainName + "' not found");
                }

                //Check username
                user = userService.findByUsername(username);
                if(user == null || !user.getUsername().equals(username)) {
                    throw new UsernameNotFoundException("User with name '" + username + "' not found");
                }

                //Check user under the current domain
                user = userService.findByUsernameAndDomain(username, domain);
                if(user == null || !user.getUsername().equals(username)) {
                    throw new UsernameNotFoundException("User with name '" + username + "' not under the domain '"
                            + domain.getAliasName() +"'");
                }

                //Check the credentials
                user = userService.findByUsernameAndPasswordAndDomain(username, password, domain);
                if(user == null) {
                    throw new BadCredentialsException("Invalid User Credentials !");
                }
            }

        }
        // If authentication to external service succeeded then create authenticated wrapper with proper Principal and GrantedAuthorities.
        // GrantedAuthorities may come from external service authentication or be hardcoded at our layer as they are here with ROLE_DOMAIN_USER
        AuthenticatedExternalWebService authenticatedExternalWebService = new AuthenticatedExternalWebService(user, null,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER"));
        authenticatedExternalWebService.setExternalWebService(externalWebService);

        return authenticatedExternalWebService;
    }
}
