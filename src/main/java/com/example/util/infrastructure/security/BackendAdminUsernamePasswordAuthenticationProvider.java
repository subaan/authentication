package com.example.util.infrastructure.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

import com.google.common.base.Optional;

/**
 * Backend admin username password authentication provider.
 *
 */
public class BackendAdminUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    /** Invalid credentials constant. */
    public static final String INVALID_BACKEND_ADMIN_CREDENTIALS = "Invalid Credentials";

    /** Token service attribute. */
    private TokenService tokenService;

    /** Admin username. */
    @Value("${backend.admin.username}")
    private String backendAdminUsername;

    /** Admin password. */
    @Value("${backend.admin.password}")
    private String backendAdminPassword;

    /**
     * Parameterized constructor.
     * @param tokenService to set
     */
    public BackendAdminUsernamePasswordAuthenticationProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        @SuppressWarnings("unchecked")
        Optional<String> username = (Optional<String>) authentication.getPrincipal();
        @SuppressWarnings("unchecked")
        Optional<String> password = (Optional<String>) authentication.getCredentials();

        if (credentialsMissing(username, password) || credentialsInvalid(username, password)) {
            throw new BadCredentialsException(INVALID_BACKEND_ADMIN_CREDENTIALS);
        }

        AuthenticationWithToken authenticationWithToken = new AuthenticationWithToken(username.get(), null,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_BACKEND_ADMIN"));
        String newToken = tokenService.generateNewToken();
        authenticationWithToken.setToken(newToken);
        tokenService.store(newToken, authenticationWithToken);
        return authenticationWithToken;
    }

    /**
     * Credentials missing.
     * @param username to set
     * @param password to set.
     * @return true/false
     */
    private boolean credentialsMissing(Optional<String> username, Optional<String> password) {
        return !username.isPresent() || !password.isPresent();
    }

    /**
     * Invalid credentials.
     * @param username to set
     * @param password to set
     * @return true/false
     */
    private boolean credentialsInvalid(Optional<String> username, Optional<String> password) {
        return !isBackendAdmin(username.get()) || !password.get().equals(backendAdminPassword);
    }

    /**
     * Is backend admin.
     * @param username to set
     * @return true/false
     */
    private boolean isBackendAdmin(String username) {
        return backendAdminUsername.equals(username);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(BackendAdminUsernamePasswordAuthenticationToken.class);
    }
}
