package com.epam.esm.dao.builder;

import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class QueryCreator implements PreparedStatementCreator {

    protected static final String WHERE = "WHERE";
    protected static final String AND = "AND";
    protected static final String OR = "OR";
    protected static final String ORDER_BY = "ORDER BY";
    protected static final String WHITESPACE = " ";

    protected abstract String buildQuery();

    protected abstract void setParameters(PreparedStatement st) throws SQLException;
}
