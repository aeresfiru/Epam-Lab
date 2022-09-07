package com.epam.esm.controller.security.jwt;

import com.epam.esm.controller.security.JwtAuthenticationException;
import com.epam.esm.domain.Role;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * JwtTokenProvider
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@Component
@PropertySource("classpath:jwt.properties")
@Slf4j
public class JwtTokenProvider {

    private static final String TOKEN_PREFIX = "Bearer ";

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expired}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.token.expired}")
    private long refreshTokenValidityInMilliseconds;

    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String username, Set<Role> roles) {
        log.info("Creating token, user: '{}', roles: {}", username, roles);

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", getRoleNames(roles));

        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        log.info("Getting authentication - token {}", token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        log.info("Getting user - token {}", token);
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            log.info("Resolving token - token {}", bearerToken);
            return bearerToken.replace(TOKEN_PREFIX, "");
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            log.info("Validating token - token {}", token);
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }

    private List<String> getRoleNames(Set<Role> userRoles) {
        List<String> result = new ArrayList<>();
        userRoles.forEach(role -> result.add(role.getName()));
        return result;
    }
}
