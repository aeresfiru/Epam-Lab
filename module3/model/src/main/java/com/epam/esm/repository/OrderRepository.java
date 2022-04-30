package com.epam.esm.repository;

import com.epam.esm.domain.Order;

import java.util.List;
import java.util.Optional;

/**
 * OrderRepository
 *
 * @author alex
 * @version 1.0
 * @since 18.04.22
 */
public interface OrderRepository {

    Optional<Order> findById(long id);

    List<Order> findUserOrders(long userId, Pagination pagination);

    Order create(Order order);

    void delete(long id);
}
