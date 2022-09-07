package com.epam.esm.controller.security;

import com.epam.esm.controller.rest.handler.RestAccessDeniedHandler;
import com.epam.esm.controller.rest.handler.RestAuthenticationEntryPoint;
import com.epam.esm.controller.security.filter.FilterChainExceptionHandler;
import com.epam.esm.controller.security.filter.JwtTokenFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * WebSecurityConfig
 *
 * @author alex
 * @version 1.0
 * @since 11.05.22
 */
@Configuration
@EnableAutoConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final FilterChainExceptionHandler filterChainExceptionHandler;

    private final JwtTokenFilter jwtTokenFilter;

    private final RestAccessDeniedHandler accessDeniedHandler;

    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v1/certificates/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/login", "/api/v1/signup").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/refresh-token").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .addFilterBefore(filterChainExceptionHandler, LogoutFilter.class)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
