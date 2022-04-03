package com.epam.esm.dao.builder;

import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public abstract class QueryCreator implements PreparedStatementCreator {

    protected enum JoinType {
        INNER, LEFT, RIGHT;
    }

    private static final String WHERE = "WHERE";
    private static final String AND = "AND";
    private static final String OR = "OR";
    private static final String ORDER_BY = "ORDER BY";
    private static final String WHITESPACE = " ";
    private static final String JOIN = "JOIN";
    private static final String COMMA = ",";

    protected StringBuilder builder;

    protected boolean hasWhereStatement = false;

    protected abstract String buildQuery();

    protected abstract void setParameters(PreparedStatement st) throws SQLException;

    protected QueryCreator where(String query) {
        this.hasWhereStatement = true;
        this.appendToBuilder(WHERE, query);
        return this;
    }

    protected QueryCreator join(String query, JoinType joinType) {
        this.appendToBuilder(joinType.name(), JOIN, query);
        return this;
    }

    protected QueryCreator or(String query) {
        this.appendToBuilder(OR, query);
        return this;
    }

    protected QueryCreator and(String query) {
        this.appendToBuilder(AND, query);
        return this;
    }

    protected QueryCreator orderBy(Map<String, SortingType> parameterSortingMap) {
        this.appendToBuilder(ORDER_BY);
        parameterSortingMap.forEach(this::attachSortingParameter);
        builder.deleteCharAt(builder.lastIndexOf(COMMA));
        return this;
    }

    private void attachSortingParameter(String key, SortingType value) {
        this.appendToBuilder(key, value.name(), COMMA);
    }

    private void appendToBuilder(String query, String... queries) {
        builder.append(WHITESPACE);
        builder.append(query);
        for (String q : queries) {
            builder.append(WHITESPACE);
            builder.append(q);
        }
    }
}
