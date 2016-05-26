package com.example.controller;

import com.example.model.CurrentlyLoggedUser;
import com.example.model.User;
import com.example.service.UserService;
import com.example.util.infrastructure.security.TokenAuthenticationProvider;
import com.example.util.web.ApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Abdul on 24/5/16.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController  {

    /**
     * Auto wired userService.
     */
    @Autowired
    private UserService userService;

    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    /** Logger constant. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String authenticate() {

        return "This is just for in-code-documentation purposes and Rest API reference documentation." +
                "Servlet will never get to this point as Http requests are processed by AuthenticationFilter." +
                "Nonetheless to authenticate Domain User POST request with X-Auth-Username and X-Auth-Password headers " +
                "is mandatory to this URL. If username and password are correct valid token will be returned (just json string in response) " +
                "This token must be present in X-Auth-Token header in all requests for all other URLs, including logout." +
                "Authentication can be issued multiple times and each call results in new ticket.";
    }

    @RequestMapping(value = "/whoami", method = RequestMethod.GET)
    public User getCurrentUser(@CurrentlyLoggedUser User currentUser) {
        LOGGER.info("Who am i: {} ", currentUser.getUsername());
       return userService.findByUsernameAndDomain(currentUser.getUsername(), currentUser.getDomain());
    }

    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            tokenAuthenticationProvider.clearToken(request);
        }
    }
}
