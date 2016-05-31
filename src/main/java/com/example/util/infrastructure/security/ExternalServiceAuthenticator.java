package com.example.util.infrastructure.security;

import com.example.model.Domain;

/**
 * External Service Authenticator.
 *
 */
public interface ExternalServiceAuthenticator {
    /**
     * Authenticate.
     *
     * @param username to set
     * @param password to set
     * @return token
     */
    AuthenticationWithToken authenticate(String username, String password, String domainName);
}
