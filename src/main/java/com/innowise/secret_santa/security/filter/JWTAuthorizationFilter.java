package com.innowise.secret_santa.security.filter;

import com.innowise.secret_santa.security.JwtToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JWTAuthorizationFilter extends GenericFilterBean {

    private final JwtToken jwtToken;

    public JWTAuthorizationFilter(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        String token = jwtToken.resolveToken((HttpServletRequest) request);

        if (token!=null && jwtToken.validToken(token)){

            Authentication authentication = jwtToken.getAuthentication(token);

            if (authentication!=null){

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}