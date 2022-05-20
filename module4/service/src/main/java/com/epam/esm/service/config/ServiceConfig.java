package com.epam.esm.service.config;

import com.epam.esm.service.ExceptionResult;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Base64;
import java.util.HashMap;

/**
 * ServiceConfig
 *
 * @author alex
 * @version 1.0
 * @since 22.04.22
 */
@Configuration
public class ServiceConfig {

    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }

    @Bean
    @Scope("prototype")
    public ExceptionResult exceptionResult() {
        return new ExceptionResult(new HashMap<>());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
