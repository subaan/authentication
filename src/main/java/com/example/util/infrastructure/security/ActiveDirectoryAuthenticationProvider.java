package com.example.util.infrastructure.security;

import com.example.model.Domain;
import com.example.model.User;
import com.example.service.ActiveDirectoryService;
import com.example.service.DomainService;
import com.example.service.UserService;
import com.example.util.activedirectory.DirectoryUtil;
import com.example.util.exceptions.DomainNameNotFoundException;
import com.example.util.infrastructure.AuthenticatedExternalWebService;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * Domain username password authentication provider.
 *
 */
public class ActiveDirectoryAuthenticationProvider implements AuthenticationProvider {

    /** Logger constant. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveDirectoryAuthenticationProvider.class);

    /** Token service attribute. */
    private TokenService tokenService;

    /** Domain service */
    @Autowired
    private DomainService domainService;

    /** User service */
    @Autowired
    private UserService userService;

    /** Active directory service */
    @Autowired
    private ActiveDirectoryService activeDirectoryService;

    /**
     * Parameterized constructor.
     * @param tokenService to set
     */
    public ActiveDirectoryAuthenticationProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        LOGGER.info("<===== AD authentication initiated =====>");

        @SuppressWarnings("unchecked")
        Optional<String> username = (Optional<String>) authentication.getPrincipal();
        @SuppressWarnings("unchecked")
        Optional<String> password = (Optional<String>) authentication.getCredentials();

        AuthenticationWithToken resultOfAuthentication = null;
        if (!username.isPresent() || !password.isPresent()) {
            throw new BadCredentialsException("Invalid User Credentials");
        }

        String userName = username.get();
        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        String domainName = session.getAttribute("domainName").toString();

        if(domainName == null || domainName.equals("")) {
            throw new DomainNameNotFoundException("Invalid User Credentials");
        }

        Domain domain = domainService.findByAliasName(domainName);
        if(domain == null) {
            throw new DomainNameNotFoundException("Invalid User Credentials");
        }
        LOGGER.info("Domain ID: {} USername: {}, password: {}", domain.getId(), userName, password);
        //Authenticate with AD
        LdapTemplate ldapTemplate = activeDirectoryService.ldapTemplate(domain.getId());
        boolean authenticate = ldapTemplate.authenticate("", DirectoryUtil.getFilter(userName, "username"), password.get());
        LOGGER.info("Authenticate: {}", authenticate);

        if(authenticate) {
            //Check user under the current domain
            User user = userService.findByUsernameAndDomain(userName, domain);
            // Set user details
            resultOfAuthentication = new AuthenticatedExternalWebService(user, null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER"));
            //Generate new token
            String newToken = tokenService.generateNewToken();
            resultOfAuthentication.setToken(newToken);
            tokenService.store(newToken, resultOfAuthentication);
            LOGGER.info("<===== User authenticated with AD succeed =====>");
        } else {
            LOGGER.info("<===== User Authentication failed =====>");
        }
        LOGGER.info("ResultOfAuthentication: {}", resultOfAuthentication);
        return resultOfAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AuthenticationWithToken.class);
    }
}
