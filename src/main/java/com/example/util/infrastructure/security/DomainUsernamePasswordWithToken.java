package com.example.util.infrastructure.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;

/**
 * Authentication with token.
 *
 */
@SuppressWarnings("serial")
public class DomainUsernamePasswordWithToken extends UsernamePasswordAuthenticationToken {

    /**
     * Parameterized constructor.
     * @param aPrincipal to set
     * @param aCredentials to set
     */
    public DomainUsernamePasswordWithToken(Object aPrincipal, Object aCredentials) {
        super(aPrincipal, aCredentials);
    }

    /**
     * Parameterized constructor.
     * @param aPrincipal to set
     * @param aCredentials to set
     * @param anAuthorities to set
     */
    public DomainUsernamePasswordWithToken(Object aPrincipal, Object aCredentials, Collection<? extends GrantedAuthority> anAuthorities) {
        super(aPrincipal, aCredentials, anAuthorities);
    }

    /**
     * Set domain name.
     * @param domainName to set
     */
    public void setDomain(String domainName) {
        setDetails(domainName);
    }

    /**
     * Get domain.
     * @return the domain name.
     */
    public String getDomain() {
        return (String)getDetails();
    }
}
