package com.epam.esm.controller.security.config;

import com.epam.esm.controller.rest.handler.RestAccessDeniedHandler;
import com.epam.esm.controller.rest.handler.RestAuthenticationEntryPoint;
import com.epam.esm.controller.security.jwt.JwtConfigurer;
import com.epam.esm.controller.security.jwt.filter.JwtOrderAccessFilter;
import com.epam.esm.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import com.epam.esm.controller.security.jwt.JwtTokenProvider;

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

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

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
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/certificates/**").permitAll()
                .antMatchers(HttpMethod.POST, "/signup", "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/orders").fullyAuthenticated()
                .antMatchers(HttpMethod.GET, "/tags/**", "/users/**").fullyAuthenticated()
                .anyRequest().hasRole("ADMIN")
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider, userService));
    }
}
