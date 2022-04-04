package com.epam.esm.dao.builder;

public abstract class AbstractQueryCreator {

    protected static final String WHERE = "WHERE";
    protected static final String AND = "AND";
    protected static final String OR = "OR";
    protected static final String ORDER_BY = "ORDER BY";
    protected static final String WHITESPACE = " ";
    protected static final String JOIN = "JOIN";
    protected static final String COMMA = ",";

    protected StringBuilder builder;
    protected boolean hasWhereStatement = false;

    public AbstractQueryCreator() {
        this.builder = new StringBuilder();
    }

    protected AbstractQueryCreator where(String query) {
        this.hasWhereStatement = true;
        this.appendToBuilder(WHERE, query);
        return this;
    }

    protected AbstractQueryCreator or(String query) {
        this.appendToBuilder(OR, query);
        return this;
    }

    protected AbstractQueryCreator and(String query) {
        this.appendToBuilder(AND, query);
        return this;
    }

    protected void removeLastComma() {
        builder.deleteCharAt(builder.lastIndexOf(COMMA));
    }

    protected void appendToBuilder(String query, String... queries) {
        builder.append(WHITESPACE);
        builder.append(query);
        for (String q : queries) {
            builder.append(WHITESPACE);
            builder.append(q);
        }
    }
}
