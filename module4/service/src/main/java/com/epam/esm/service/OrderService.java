package com.epam.esm.service;

import com.epam.esm.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * OrderService
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
public interface OrderService {

    Order findById(Long orderId);

    Page<Order> findUserOrders(Long userId, Pageable pageable);

    Order create(Order order);

    void deleteById(Long orderId);
}
