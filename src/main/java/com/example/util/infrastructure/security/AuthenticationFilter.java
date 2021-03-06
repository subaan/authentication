package com.example.util.infrastructure.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.constants.GenericConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import com.example.util.web.ApiController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

/**
 * Authentication filter.
 *
 */
public class AuthenticationFilter extends GenericFilterBean {

    /** Logger constant. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    /** Token session key constant. */
    public static final String TOKEN_SESSION_KEY = "token";

    /** User session constant. */
    public static final String USER_SESSION_KEY = "user";

    /** Authentication manager attribute. */
    private AuthenticationManager authenticationManager;

    /** Admin username. */
    @Value("${backend.admin.username}")
    private String backendAdminUsername;

    /** External service authenticator. */
    private ExternalServiceAuthenticator externalServiceAuthenticator;

    /** Active  directory authenticator provider. */
    private ActiveDirectoryAuthenticationProvider activeDirectoryAuthenticationProvider;

    /**
     * Parameterized constructor.
     * @param activeDirectoryAuthenticationProvider to set
     * @param authenticationManager to set
     */
    public AuthenticationFilter(ActiveDirectoryAuthenticationProvider activeDirectoryAuthenticationProvider, AuthenticationManager authenticationManager) {
        this.activeDirectoryAuthenticationProvider = activeDirectoryAuthenticationProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);

//        Optional<String> username = Optional.fromNullable(httpRequest.getHeader(GenericConstants.AUTHENTICATION_HEADER_USERNAME));
//        Optional<String> password = Optional.fromNullable(httpRequest.getHeader(GenericConstants.AUTHENTICATION_HEADER_PASSWORD));
        Optional<String> token = Optional.fromNullable(httpRequest.getHeader(GenericConstants.AUTHENTICATION_HEADER_TOKEN));


        String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);

        try {
            if (postToAuthenticate(httpRequest, resourcePath)) {

                StringBuffer jb = new StringBuffer();
                String line = null;
                try {
                    BufferedReader reader = request.getReader();
                    while ((line = reader.readLine()) != null)
                        jb.append(line);
                } catch (Exception e) { /*report an error*/ }
                String domainName = "";
                Optional<String> username = Optional.absent();
                Optional<String> password = Optional.absent();
                if(jb.toString() != null && !jb.toString().isEmpty()) {

                    // Convert String to Json Object.
                    JsonReader jsonReader = Json.createReader(new StringReader(jb.toString()));
                    JsonObject object = jsonReader.readObject();

                    // Get domain name,username and password from json string.
                    domainName = object.get(GenericConstants.DOMAIN_NAME) == null ? "" :
                            object.getString(GenericConstants.DOMAIN_NAME);
                    String jsonUsername = object.get(GenericConstants.USERNAME) == null ? "" :
                            object.getString(GenericConstants.USERNAME);
                    String jsonPassword = object.get(GenericConstants.PASSWORD) == null ? "" :
                            object.getString(GenericConstants.PASSWORD);
                    username = Optional.fromNullable(jsonUsername) ;
                    password = Optional.fromNullable(jsonPassword);

                }

                // Set domain name to session
//                HttpSession session = httpRequest.getSession(true);
//                session.setAttribute("domainName", domainName);

                LOGGER.info("Trying to authenticate user {} by X-Auth-Username method", username);
                processUsernamePasswordAuthentication(httpResponse, username, password, domainName);
                return;
            }

            if (token.isPresent()) {
                LOGGER.debug("Trying to authenticate user by X-Auth-Token method. Token: {}", token);
                processTokenAuthentication(token);
            }

            LOGGER.debug("AuthenticationFilter is passing request down the filter chain");
            addSessionContextToLogging();
            chain.doFilter(request, response);
        } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            SecurityContextHolder.clearContext();
            LOGGER.error("Internal authentication service exception", internalAuthenticationServiceException);
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        } finally {
            MDC.remove(TOKEN_SESSION_KEY);
            MDC.remove(USER_SESSION_KEY);
        }
    }

    /** Add session context to logging. */
    private void addSessionContextToLogging() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tokenValue = "EMPTY";
        if (authentication != null && !Strings.isNullOrEmpty(authentication.getDetails().toString())) {
            MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-1");
            tokenValue = encoder.encodePassword(authentication.getDetails().toString(), "not_so_random_salt");
        }
        MDC.put(TOKEN_SESSION_KEY, tokenValue);

        String userValue = "EMPTY";
        if (authentication != null && !Strings.isNullOrEmpty(authentication.getPrincipal().toString())) {
            userValue = authentication.getPrincipal().toString();
        }
        MDC.put(USER_SESSION_KEY, userValue);
    }

    /**
     * Convert servlet request to http servlet request.
     * @param request to set
     * @return HttpServletRequest
     */
    private HttpServletRequest asHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    /**
     * Convert servlet response to http servlet response.
     * @param response to set
     * @return HttpServletResponse
     */
    private HttpServletResponse asHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

    /**
     * Post to authentivate.
     * @param httpRequest to set
     * @param resourcePath to set
     * @return true/false
     */
    private boolean postToAuthenticate(HttpServletRequest httpRequest, String resourcePath) {
        return ApiController.AUTHENTICATE_URL.equalsIgnoreCase(resourcePath) && httpRequest.getMethod().equals("POST");
    }

    /**
     * Process authentication.
     *
     * @param httpResponse to set
     * @param username to set
     * @param password to set
     * @throws IOException if any error occurs
     */
    private void processUsernamePasswordAuthentication(HttpServletResponse httpResponse, Optional<String> username,
                                                       Optional<String> password, String domainName) throws IOException {
        Authentication resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(username, password, domainName);
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        TokenResponse tokenResponse = new TokenResponse(resultOfAuthentication.getDetails().toString());
        String tokenJsonResponse = new ObjectMapper().writeValueAsString(tokenResponse);
        httpResponse.addHeader(GenericConstants.CONTENT_TYPE_HEADER, GenericConstants.CONTENT_TYPE_JSON);
        httpResponse.addHeader(GenericConstants.AUTHENTICATION_HEADER_TOKEN, resultOfAuthentication.getDetails().toString());
//        httpResponse.getWriter().print(tokenJsonResponse);
    }

    /**
     * Try to authenticate with username and password.
     * @param username to set
     * @param password to set
     * @param domainName to set
     * @return Authentication
     */
    private Authentication tryToAuthenticateWithUsernameAndPassword(Optional<String> username, Optional<String> password,
                                                                    String domainName) {
        if(domainName == null || domainName.isEmpty()) {
            //Authenticate application admin
            BackendAdminUsernamePasswordAuthenticationToken backendAdminUsernamePasswordAuthenticationToken = new BackendAdminUsernamePasswordAuthenticationToken(username, password);
            return tryToAuthenticate(backendAdminUsernamePasswordAuthenticationToken);
        } else {
            boolean authenticateByAd = true;
            Authentication authentication = null;
            if(authenticateByAd) {
                //Active directory authenticated first
                ActiveDirectoryAuthenticationWithToken requestAuthentication = new ActiveDirectoryAuthenticationWithToken(username, password);
                requestAuthentication.setDomain(domainName);
                authentication = tryToAuthenticate(requestAuthentication);
            }

            if(!authenticateByAd || (authenticateByAd && !authentication.isAuthenticated())) { //If AD authentication fail, DB auth provider called
                DomainUsernamePasswordWithToken domainUsernamePasswordWithToken = new DomainUsernamePasswordWithToken(username, password);
                domainUsernamePasswordWithToken.setDomain(domainName);
                authentication = tryToAuthenticate(domainUsernamePasswordWithToken);
            }

            return authentication;
        }

    }

    /**
     * Process token authentication.
     * @param token to set
     */
    private void processTokenAuthentication(Optional<String> token) {
        Authentication resultOfAuthentication = tryToAuthenticateWithToken(token);
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
    }

    /**
     * Try to authenticate with token.
     * @param token to set
     * @return Authentication
     */
    private Authentication tryToAuthenticateWithToken(Optional<String> token) {
        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token, null);
        return tryToAuthenticate(requestAuthentication);
    }

    /**
     * Try to authenticate.
     * @param requestAuthentication to set
     * @return Authentication
     */
    private Authentication tryToAuthenticate(Authentication requestAuthentication) {
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
//        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
//            throw new BadCredentialsException("Unable to authenticate Domain User for provided credentials");
//        }
        LOGGER.debug("User successfully authenticated");
        return responseAuthentication;
    }
}
