package com.epam.esm.dao.builder.update;

import com.epam.esm.dao.builder.AbstractQueryCreator;
import org.apache.commons.lang3.StringUtils;
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

    protected UpdateQueryCreator set(Map<String, String> paramValues) {
        this.appendToBuilder(SET);
        paramValues.forEach((k, v) -> {
            if (StringUtils.isNumeric(v))
                this.appendToBuilder(k, EQUAL, v, COMMA);
            else {
                String value = QUOTE + v + QUOTE;
                this.appendToBuilder(k, EQUAL, value, COMMA);
            }
        });
        this.removeLastComma();
        return this;
    }
}
