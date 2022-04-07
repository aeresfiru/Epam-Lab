package com.epam.esm.dao.query;

import org.springframework.stereotype.Component;

/**
 * SelectQueryBuilder
 *
 * @author alex
 * @version 1.0
 * @since 8.04.22
 */
@Component
public class SelectQueryCreator extends QueryCreator {

    @Override
    public String createSqlQuery() {
        StringBuilder query = new StringBuilder();

        this.setSelectStatement(query);
        this.setJoinsStatement(query);
        this.setWhereStatement(query);
        this.setOrderByStatement(query);
        this.setLimitStatement(query);

        query.append(";");
        return query.toString();
    }

    private void setOrderByStatement(StringBuilder query) {
        if (orderValues != null && !orderValues.isEmpty()) {
            query.append(" " + ORDER_BY + " ");
            orderValues.forEach((k, v) -> this.attachSortingParameter(query, k, v));
            query.deleteCharAt(query.lastIndexOf(","));
        }
    }

    private void attachSortingParameter(StringBuilder query, String key, SortingType sortingType) {
        query.append(key).append(" ").append(sortingType).append(", ");
    }

    private void setLimitStatement(StringBuilder query) {
        if (offsetValue > 0 && limitValue > 0) {
            query.append(" " + LIMIT + " ").append(offsetValue).append(", ").append(limitValue);
        }
        if (limitValue > 0) {
            query.append(" " + LIMIT + " ").append(limitValue);
        }
    }

    private void setJoinsStatement(StringBuilder query) {
        if (!joinInfos.isEmpty()) {
            for (JoinInfo info : joinInfos) {
                query.append(" ").append(info.getJoinType().name()).append(" ").append(JOIN);
                query.append(" ").append(info.getTable());
                query.append(" ").append(ON);
                query.append(" ").append(info.getLeftField()).append(" = ").append(info.getRightField());
            }
        }
    }

    private void setSelectStatement(StringBuilder query) {
        query.append(SELECT).append(" ");
        if (isDistinct) {
            query.append(DISTINCT).append(" ");
        }
        for (String field : tableFields) {
            query.append(field).append(", ");
        }
        query.deleteCharAt(query.lastIndexOf(","));
        query.append(FROM).append(" ").append(tableName);
    }
}
