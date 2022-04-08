package com.epam.esm.dao.query;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * JoinInfo
 *
 * @author alex
 * @version 1.0
 * @since 7.04.22
 */
@Data
@AllArgsConstructor
public final class JoinInfo {
    public enum JoinType {
        LEFT, RIGHT, INNER, OUTER
    }

    private String leftField;
    private String rightField;
    private String table;
    private JoinType joinType;
}
