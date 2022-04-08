package com.epam.esm.dao.query;

import java.util.Map;

/**
 * UpdateQueryBuilder
 *
 * @author alex
 * @version 1.0
 * @since 8.04.22
 */
public final class UpdateQueryCreator extends QueryCreator {

    @Override
    public String createSqlQuery() {
        StringBuilder query = new StringBuilder();
        query.append(UPDATE).append(" ").append(tableName).append(" ").append(SET).append(" ");

        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.append(entry.getKey())
                    .append(" = ")
                    .append(getSqlFieldString(entry.getValue()))
                    .append(", ");
        }
        query.deleteCharAt(query.lastIndexOf(","));

        this.setWhereStatement(query);

        query.append(";");
        return query.toString();
    }
}
