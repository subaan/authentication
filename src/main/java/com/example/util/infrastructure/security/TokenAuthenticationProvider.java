package com.example.util.infrastructure.security;

import com.example.constants.GenericConstants;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.google.common.base.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * Token authentication provider.
 *
 */
public class TokenAuthenticationProvider implements AuthenticationProvider {

    /** Token service attribute. */
    private TokenService tokenService;

    /**
     * Parameterized constructor.
     * @param tokenService to set
     */
    public TokenAuthenticationProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        @SuppressWarnings("unchecked")
        Optional<String> token = (Optional<String>) authentication.getPrincipal();

        if (!token.isPresent() || token.get().isEmpty()) {
            throw new BadCredentialsException("Invalid token");
        }
        if (!tokenService.contains(token.get())) {
            throw new BadCredentialsException("Invalid token or token expired");
        }
        return tokenService.retrieve(token.get());
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}
