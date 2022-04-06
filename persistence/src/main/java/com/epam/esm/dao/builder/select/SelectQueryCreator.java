package com.epam.esm.dao.builder.select;

import com.epam.esm.dao.builder.AbstractQueryCreator;
import com.epam.esm.dao.builder.SortingType;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.util.Map;

public abstract class SelectQueryCreator extends AbstractQueryCreator
        implements PreparedStatementCreator {

    protected enum JoinType {
        INNER, LEFT, RIGHT;
    }

    protected SelectQueryCreator join(String query, JoinType joinType) {
        this.appendToBuilder(joinType.name(), JOIN, query);
        return this;
    }

    protected SelectQueryCreator join(String query) {
        this.appendToBuilder(JOIN, query);
        return this;
    }

    protected SelectQueryCreator orderBy(Map<String, SortingType> parameterSortingMap) {
        this.appendToBuilder(ORDER_BY);
        parameterSortingMap.forEach(this::attachSortingParameter);
        this.removeLastComma();
        return this;
    }

    private void attachSortingParameter(String key, SortingType value) {
        this.appendToBuilder(key, value.name(), COMMA);
    }

    protected String defineWhereOrAnd() {
        if (hasWhereStatement) {
            return AND;
        } else {
            return WHERE;
        }
    }
}
