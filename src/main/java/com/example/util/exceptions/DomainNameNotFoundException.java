package com.example.util.exceptions;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Thrown if an domain name not found.
 *
 * Created by Abdul on 25/5/16.
 */
public class DomainNameNotFoundException extends AuthenticationException {


    /**
     * Constructs a <code>DomainNameNotFoundException</code> with the specified message.
     *
     * @param msg the detail message.
     */
    public DomainNameNotFoundException(String msg) {
        super(msg);
    }

    /**
     * Constructs a {@code DomainNameNotFoundException} with the specified message and root
     * cause.
     *
     * @param msg the detail message.
     * @param t root cause
     */
    public DomainNameNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
