package com.innowise.secret_santa.security;

import com.innowise.secret_santa.exception.NoAccessException;
import com.innowise.secret_santa.model.dto.RoleDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static com.innowise.secret_santa.constants_message.Constants.HEADER_AUTHORIZATION;

@Component
public class JwtToken {

    private final UserSecurityService userDetailsService;

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private long validityInMilliseconds;

    public JwtToken(UserSecurityService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String getJWTToken(String username, List<RoleDto> roles) {

        Claims claims = Jwts.claims().setSubject(username);
        int count = 1;
        for (RoleDto role : roles) {
            claims.put("role "+count++, role.getRoleName().getRole());
        }


        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    private String getUserName(String token) {

        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }


    public String resolveToken(HttpServletRequest request) {

        return request.getHeader(HEADER_AUTHORIZATION);
    }

    public boolean validToken(String token) {

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());

        } catch (Exception exception) {

            throw new NoAccessException("Jwt token is expired " + exception.getMessage());
        }
    }


    public Authentication getAuthentication(String token) {

        UserSecurity userDetails = (UserSecurity) userDetailsService.loadUserByUsername(getUserName(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}