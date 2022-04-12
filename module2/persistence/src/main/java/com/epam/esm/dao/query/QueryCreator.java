package com.epam.esm.dao.query;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public abstract class QueryCreator {
    protected static final String SELECT = "SELECT";
    protected static final String INSERT_INTO = "INSERT INTO";
    protected static final String UPDATE = "UPDATE";
    protected static final String DELETE = "DELETE";

    protected static final String FROM = "FROM";
    protected static final String WHERE = "WHERE";
    protected static final String AND = "AND";
    protected static final String SET = "SET";
    protected static final String VALUES = "VALUES";
    protected static final String OR = "OR";

    protected static final String NULL = "NULL";
    protected static final String LIMIT = "LIMIT";
    protected static final String JOIN = "JOIN";
    protected static final String ON = "ON";
    protected static final String ORDER_BY = "ORDER BY";
    protected static final String DISTINCT = "DISTINCT";

    protected String tableName;

    protected List<String> tableFields;

    protected Map<String, Object> parameters;

    protected List<WhereInfo> wheres;

    protected List<JoinInfo> joinInfos;

    protected int limitValue = 0;

    protected int offsetValue = 0;

    protected Map<String, SortingType> orderValues;

    protected boolean isDistinct;

    protected QueryCreator() {
        parameters = new HashMap<>();
        wheres = new ArrayList<>();
        joinInfos = new ArrayList<>();
        tableFields = new ArrayList<>();
        orderValues = new HashMap<>();
    }

    public abstract String createSqlQuery();

    public void tableName(String tableName) {
        this.tableName = tableName;
    }

    public QueryCreator tableFields(String... fields) {
        tableFields.addAll(Arrays.asList(fields));
        return this;
    }

    public QueryCreator setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
        return this;
    }

    public QueryCreator addParameter(String field, Object value) {
        this.parameters.put(field, value);
        return this;
    }

    public QueryCreator addWhere(String field, String value,
                                 WhereInfo.WhereOperator operator,
                                 boolean startBracket,
                                 boolean endBracket,
                                 boolean isOreClause) {
        wheres.add(new WhereInfo(operator, field, value, startBracket, endBracket, isOreClause));
        return this;
    }

    public QueryCreator addWhere(String field, String value,
                                 WhereInfo.WhereOperator operator) {
        wheres.add(new WhereInfo(operator, field, value, false, false, false));
        return this;
    }

    public QueryCreator addLimit(int limit, int offset) {
        this.limitValue = limit;
        this.offsetValue = offset;
        return this;
    }

    public QueryCreator addLimit(int limit) {
        this.limitValue = limit;
        return this;
    }

    public QueryCreator join(String tableName, String leftField, String rightField) {
        joinInfos.add(new JoinInfo(leftField, rightField, tableName, JoinInfo.JoinType.INNER));
        return this;
    }

    public QueryCreator join(String tableName, String leftField, String rightField, JoinInfo.JoinType joinType) {
        joinInfos.add(new JoinInfo(leftField, rightField, tableName, joinType));
        return this;
    }

    public void orderBy(Map<String, SortingType> orderValues) {
        this.orderValues = orderValues;
    }

    public void distinct() {
        this.isDistinct = true;
    }

    protected String getSqlFieldString(Object field) {
        String str;

        if (field == null) {
            str = NULL;
        } else if (StringUtils.isNumeric(field.toString())) {
            str = field.toString();
        } else {
            str = "'" + field + "'";
        }

        return str;
    }

    protected void setWhereStatement(StringBuilder query) {
        if (wheres.isEmpty()) {
            return;
        }
        int fieldCount = 0;
        query.append(" ").append(WHERE).append(" ");

        for (WhereInfo whereInfo : wheres) {
            if (fieldCount != 0) {
                if (whereInfo.isOrClause()) {
                    query.append(" " + OR);
                } else {
                    query.append(" " + AND);
                }
            }
            if (whereInfo.isStartBracket()) {
                query.append("(");
            }
            query.append(" ").append(whereInfo.getField())
                    .append(" ").append(whereInfo.getOperator().toString())
                    .append(" ").append(getSqlFieldString(whereInfo.getValue()));
            if (whereInfo.isEndBracket()) {
                query.append(")");
            }
            fieldCount++;
        }
    }
}
