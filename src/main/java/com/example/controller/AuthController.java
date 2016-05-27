package com.example.controller;

import com.example.model.CurrentUser;
import com.example.model.User;
import com.example.service.UserService;
import com.example.util.infrastructure.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Abdul on 24/5/16.
 */
@RestController
@RequestMapping("/api/auth")
@Component
public class AuthController  {

    /**
     * User managements service.
     */
    @Autowired
    private UserService userService;

    /**
     * Token management service.
     */
    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String authenticate() {

        return "";
    }

    /**
     * This method is used to provide complete information of current user.
     *
     * @param currentUser the current login user
     * @return the current user.
     */
    @RequestMapping(value = "/whoami", method = RequestMethod.GET)
    public User getCurrentUser(@CurrentUser User currentUser) {
       return userService.findByUsernameAndDomain(currentUser.getUsername(), currentUser.getDomain());
    }

    /**
     * This method is used to clear the session and remove token.
     *
     * @param request the http request
     * @param response the http response
     */
    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public void logout(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            //Remove token
            tokenService.clearToken(request);
        }
    }
}
