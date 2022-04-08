package com.epam.esm.dao.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * WhereOperator
 *
 * @author alex
 * @version 1.0
 * @since 8.04.22
 */
@Data
@Builder
@AllArgsConstructor
public final class WhereInfo {
    private final WhereOperator operator;
    private final String field;
    private final String value;
    private final boolean startBracket;
    private final boolean endBracket;
    private final boolean orClause;
    public enum WhereOperator {
        EQUALS("="),
        NOT_EQUALS("<>"),
        LESS_THAN("<"),
        GREATER_THAN(">"),
        LESS_THAN_EQUALS("<="),
        GREATER_THAN_EQUALS(">="),
        LIKE("LIKE"),
        IS("IS"),
        NOT_LIKE("NOT LIKE");

        private final String value;

        WhereOperator(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
