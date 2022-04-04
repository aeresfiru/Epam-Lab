package com.epam.esm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("com.epam.esm")
public class WebConfig implements WebMvcConfigurer {

/*    @Bean
    @Qualifier("errorMessageSource")
    public MessageSource errorMessageSource(
            @Value("${app.errorMessagesFilename}") String resourceBundleBaseName,
            @Value("${app.defaultEncoding}") String defaultEncoding) {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename(resourceBundleBaseName);
        source.setDefaultEncoding(defaultEncoding);
        return source;
    }*/
}
