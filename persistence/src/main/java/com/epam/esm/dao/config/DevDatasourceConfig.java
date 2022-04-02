package com.epam.esm.dao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application-dev.properties")
public class DevDatasourceConfig extends ProdDatasourceConfig
        implements DatasourceConfig {

    @Value("${script.structure}")
    private String dbStructureScript;

    @Value("${script.data}")
    private String dbDataScript;

    @Value("${encoding}")
    private String encoding;

    @Override
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding(encoding)
                .addScript(dbStructureScript)
                .addScript(dbDataScript)
                .build();
    }
}
