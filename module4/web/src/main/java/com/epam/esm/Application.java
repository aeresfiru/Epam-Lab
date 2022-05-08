package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Application
 *
 * @author alex
 * @version 1.0
 * @since 25.04.22
 */
@SpringBootApplication
@Import(CertificatesApplication.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
