package com.innowise.secret_santa.config;

import com.innowise.secret_santa.exception.NoAccessException;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.List;

@Configuration
@SuppressWarnings("unchecked")
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {

        List<SimpleGrantedAuthority> roles = (List<SimpleGrantedAuthority>) permission;
        boolean b = roles.stream()
                .anyMatch(role -> role.getAuthority().equals(targetDomainObject));
        if (targetDomainObject != null && b) {
            return true;
        }

        throw new NoAccessException("You don't have enough rights");
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        return (auth != null) && (targetType != null) && permission instanceof String;
    }
}