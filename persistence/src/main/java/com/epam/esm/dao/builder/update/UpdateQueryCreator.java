package com.epam.esm.dao.builder.update;

import com.epam.esm.dao.builder.AbstractQueryCreator;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.util.Map;

public abstract class UpdateQueryCreator extends AbstractQueryCreator
        implements PreparedStatementCreator {

    private static final String UPDATE = "UPDATE";
    private static final String SET = "SET";
    private static final String EQUAL = "=";
    private static final String QUOTE = "'";

    protected UpdateQueryCreator update(String tableName) {
        this.appendToBuilder(UPDATE, tableName);
        return this;
    }

    protected UpdateQueryCreator set(Map<String, Object> paramValues) {
        this.appendToBuilder(SET);
        paramValues.forEach((k, v) -> {
            String value = QUOTE + v + QUOTE;
            this.appendToBuilder(k, EQUAL, value, COMMA);
        });
        this.removeLastComma();
        return this;
    }
}
