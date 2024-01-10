package com.innowise.secret_santa.util;

import com.innowise.secret_santa.exception.UnauthorizedException;
import com.innowise.secret_santa.exception.NoAccessException;
import com.innowise.secret_santa.security.UserSecurity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class HandleAuthorities {

    private HandleAuthorities() {
    }

    public static Long getIdAuthenticationAccount() {
        return Optional.ofNullable(getPrincipal())
                .map(UserSecurity::getId)
                .orElseThrow(() -> new NoAccessException("Unknown identification account"));
    }

    private static UserSecurity getPrincipal() {

        try {
            return Optional.ofNullable
                            ((UserSecurity) SecurityContextHolder
                                    .getContext()
                                    .getAuthentication()
                                    .getPrincipal())
                    .orElseThrow();

        } catch (ClassCastException | NullPointerException exception) {
            throw new UnauthorizedException("You don't authentication");
        }
    }
}