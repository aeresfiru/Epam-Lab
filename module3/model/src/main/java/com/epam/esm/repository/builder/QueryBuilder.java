package com.epam.esm.repository.builder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * QueryBuilder
 *
 * @author alex
 * @version 1.0
 * @since 19.04.22
 */
public interface QueryBuilder<T> {

    CriteriaQuery<T> selectQuery(PageSettings pageSettings, CriteriaBuilder criteriaBuilder);
}
