package com.epam.esm.dao.config;


import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
@Import({DevDatasourceConfig.class, ProdDatasourceConfig.class})
public class DatasourceConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public SimpleJdbcCall simpleJdbcCall(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcCall(jdbcTemplate);
    }

    @Bean
    public RowMapper<Certificate> certificateRowMapper() {
        return new BeanPropertyRowMapper<>(Certificate.class);
    }

    @Bean
    public RowMapper<Tag> tagRowMapper() {
        return new BeanPropertyRowMapper<>(Tag.class);
    }
}
