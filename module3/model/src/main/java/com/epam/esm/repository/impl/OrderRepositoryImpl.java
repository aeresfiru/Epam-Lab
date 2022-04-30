package com.epam.esm.repository.impl;

import com.epam.esm.domain.Order;
import com.epam.esm.repository.AbstractRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.builder.QueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OrderRepositoryImpl
 *
 * @author alex
 * @version 1.0
 * @since 18.04.22
 */
@Slf4j
@Repository
public class OrderRepositoryImpl extends AbstractRepository<Order>
        implements OrderRepository {

    private static final String SELECT_USER_ORDERS = "Select o FROM Order o WHERE o.user.id = :user_id";

    protected OrderRepositoryImpl() {
        super(Order.class);
    }

    @Override
    protected QueryBuilder<Order> getQueryBuilder() {
        throw new UnsupportedOperationException("Operation unsupported");
    }

    @Override
    public List<Order> findUserOrders(long userId, Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage() - 1, pagination.getPageSize());
        @SuppressWarnings("unchecked")
        List<Order> orders = em.createQuery(SELECT_USER_ORDERS)
                .setParameter("user_id", userId)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        return orders;
    }
}
