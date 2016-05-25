package com.example.util.infrastructure.externalwebservice;

import com.example.model.Domain;
import org.springframework.security.core.authority.AuthorityUtils;

import com.example.model.User;
import com.example.util.infrastructure.AuthenticatedExternalWebService;
import com.example.util.infrastructure.security.AuthenticationWithToken;
import com.example.util.infrastructure.security.ExternalServiceAuthenticator;

/**
 * External service authenticator.
 *
 */
public class SomeExternalServiceAuthenticator implements ExternalServiceAuthenticator {

    @Override
    public AuthenticationWithToken authenticate(String username, String password, Domain domain) {
        ExternalWebServiceStub externalWebService = new ExternalWebServiceStub();

        // Do all authentication mechanisms required by external web service protocol and validated response.
        // Throw descendant of Spring AuthenticationException in case of unsucessful authentication. For example BadCredentialsException

        // ...
        // ...

        // If authentication to external service succeeded then create authenticated wrapper with proper Principal and GrantedAuthorities.
        // GrantedAuthorities may come from external service authentication or be hardcoded at our layer as they are here with ROLE_DOMAIN_USER
        AuthenticatedExternalWebService authenticatedExternalWebService = new AuthenticatedExternalWebService(new User(username, domain), null,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER"));
        authenticatedExternalWebService.setExternalWebService(externalWebService);

        return authenticatedExternalWebService;
    }
}
