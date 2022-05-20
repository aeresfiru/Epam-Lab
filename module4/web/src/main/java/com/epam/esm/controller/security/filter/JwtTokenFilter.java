package com.epam.esm.controller.security.jwt.filter;

import com.epam.esm.controller.security.JwtAuthenticationException;
import com.epam.esm.controller.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JwtTokenFilter
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@Component
@Slf4j
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws ServletException, IOException {

        log.info("IN doFilterInternal");
        String token = jwtTokenProvider.resolveToken(req);

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            log.info("IN doFilterInternal - token is null or invalid");
            chain.doFilter(req, resp);
            return;
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(req, resp);
    }
}
