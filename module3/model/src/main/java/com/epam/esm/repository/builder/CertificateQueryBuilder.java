package com.epam.esm.repository.builder;

import com.epam.esm.domain.Certificate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * CertificateQueryBuilder
 *
 * @author alex
 * @version 1.0
 * @since 19.04.22
 */
@Component
@Slf4j
public class CertificateQueryBuilder implements QueryBuilder<Certificate> {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String TAGS = "tags";

    @Override
    public CriteriaQuery<Certificate> selectQuery(PageSettings pageSettings,
                                                  CriteriaBuilder cb) {

        CertificatePageSettings settings = (CertificatePageSettings) pageSettings;

        CriteriaQuery<Certificate> criteriaQuery = cb.createQuery(Certificate.class);
        Root<Certificate> from = criteriaQuery.from(Certificate.class);
        criteriaQuery.select(from).distinct(true);

        List<Predicate> restrictions = new LinkedList<>();
        if (settings.getTagParam() != null && !settings.getTagParam().isEmpty()) {
            List<Predicate> tagPredicates = this.addTagsPredicate(cb, from, settings.getTagParam());
            Predicate tagPredicate = cb.and(tagPredicates.toArray(new Predicate[0]));
            restrictions.add(tagPredicate);
        }

        if (settings.getSearchQuery() != null && !settings.getSearchQuery().isEmpty()) {
            String toSearch = "%" + settings.getSearchQuery() + "%";
            Predicate searchPredicate = this.addSearchPredicate(cb, from, toSearch);
            restrictions.add(searchPredicate);
        }

        if (!restrictions.isEmpty()) {
            criteriaQuery.where(restrictions.toArray(new Predicate[]{}));
        }

        if (settings.getParameterSortingTypeMap() != null && !settings.getParameterSortingTypeMap().isEmpty()) {
            List<Order> orderFields = this.createOrderFields(cb, from, settings);
            criteriaQuery.orderBy(orderFields);
        }

        return criteriaQuery;
    }

    private List<Order> createOrderFields(CriteriaBuilder cb,
                                          Root<Certificate> from,
                                          CertificatePageSettings config) {
        Map<String, SortType> orderValues = config.getParameterSortingTypeMap();
        List<Order> orders = new LinkedList<>();
        if (orderValues != null && !orderValues.isEmpty()) {
            for (Map.Entry<String, SortType> entry : orderValues.entrySet()) {
                Order order;
                if (SortType.ASC.equals(entry.getValue())) {
                    order = cb.asc(from.get(entry.getKey()));
                } else {
                    order = cb.desc(from.get(entry.getKey()));
                }
                orders.add(order);
            }
        }
        return orders;
    }

    private Predicate addSearchPredicate(CriteriaBuilder cb,
                                         Root<Certificate> root,
                                         String toSearch) {
        Predicate name = cb.like(cb.upper(root.get(NAME)), toSearch.toUpperCase());
        Predicate description = cb.like(cb.upper(root.get(DESCRIPTION)), toSearch.toUpperCase());
        return cb.or(name, description);
    }

    private List<Predicate> addTagsPredicate(CriteriaBuilder cb,
                                             Root<Certificate> root,
                                             List<String> tags) {
        List<Predicate> restrictions = new ArrayList<>();

        if (tags != null) {
            for (String tagName : tags) {
                restrictions.add(cb.equal(cb.upper(root.join(TAGS).get(NAME)), tagName.toUpperCase()));
            }
        }
        return restrictions;
    }
}
