package com.example.model;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal
/**
 * This is used to get current user detail from the token.
 *
 * Created by Abdul on 23/5/16.
 */
public @interface CurrentUser {
}


