package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application-dev.properties")
@Profile("dev")
public class DevDatasourceConfiguration {

    @Bean
    public DataSource dataSource() {

        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(false)
                .setName("gifts-db;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1")
                .setType(EmbeddedDatabaseType.H2)
                .addDefaultScripts()
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .build();
    }
}
