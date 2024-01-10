package com.innowise.secret_santa.security;

import com.innowise.secret_santa.security.filter.JWTAuthorizationFilter;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtToken jwtToken;

    public JwtConfigurer(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public void configure(HttpSecurity httpSecurity){
        JWTAuthorizationFilter jwtTokenFilter = new JWTAuthorizationFilter(jwtToken);
        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}