package com.example.util.infrastructure.security;

import java.util.UUID;

import com.example.constants.GenericConstants;
import com.google.common.base.Optional;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

/**
 * Token Service.
 *
 */
public class TokenService {

    /** Logger constant. */
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    /** REST API auth token constant. */
    private static final Cache REST_API_AUTH_TOKEN = CacheManager.getInstance().getCache("restApiAuthTokenCache");

    /** Scheduler time constant. */
    public static final int HALF_AN_HOUR_IN_MILLISECONDS = 30 * 60 * 1000;

    /**
     * Evict expire tokens.
     */
    @Scheduled(fixedRate = HALF_AN_HOUR_IN_MILLISECONDS)
    public void evictExpiredTokens() {
        LOGGER.info("Evicting expired tokens");
        REST_API_AUTH_TOKEN.evictExpiredElements();
    }

    /**
     * Generate new token.
     * @return token
     */
    public String generateNewToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Store token.
     * @param token to set
     * @param authentication to set
     */
    public void store(String token, Authentication authentication) {
        REST_API_AUTH_TOKEN.put(new Element(token, authentication));
    }

    /**
     * Check token already exists.
     * @param token to set
     * @return true/false.
     */
    public boolean contains(String token) {
        return REST_API_AUTH_TOKEN.get(token) != null;
    }

    /**
     * Get the auth token.
     * @param token to set
     * @return Authentication
     */
    public Authentication retrieve(String token) {
        return (Authentication) REST_API_AUTH_TOKEN.get(token).getObjectValue();
    }

    /**
     * Get the auth token.
     * @param token to set
     */
    public void remove(String token) {
        CacheManager.getInstance().getCache("restApiAuthTokenCache")
                .remove(REST_API_AUTH_TOKEN.get(token).getObjectKey());
    }

    /**
     * Used to remove token form cache.
     *
     * @param request the http request.
     * @throws org.springframework.security.core.AuthenticationException
     */
    public void clearToken(HttpServletRequest request) throws AuthenticationException {
        Optional<String> token = Optional.fromNullable(request.getHeader(GenericConstants.AUTHENTICATION_HEADER_TOKEN));
        if (!token.isPresent() || token.get().isEmpty()) {
            throw new BadCredentialsException("Invalid token");
        }

        if (!this.contains(token.get())) {
            throw new BadCredentialsException("Invalid token or token expired");
        }
        //Remove token
        this.remove(token.get());
    }
}
