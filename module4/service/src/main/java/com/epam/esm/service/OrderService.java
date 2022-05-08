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

    Order findByUserIdAndOrderId(Long userId, Long orderId) throws IllegalAccessException;

    Order findById(Long orderId);

    Page<Order> findUserOrders(long userId, Pageable pageable);

    Order create(Order dto);

    void deleteById(Long orderId);

    void deleteById(Long userId, Long orderId) throws IllegalAccessException;
}
