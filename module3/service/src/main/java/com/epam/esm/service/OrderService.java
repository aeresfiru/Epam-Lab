package com.epam.esm.service;

import com.epam.esm.dto.CreateOrderDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.repository.Pagination;

import java.util.List;

/**
 * OrderService
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
public interface OrderService {

    OrderDto findById(Long userId, Long orderId) throws IllegalAccessException;

    OrderDto findById(Long orderId);

    List<OrderDto> findUserOrders(long userId, Pagination pagination);

    OrderDto create(CreateOrderDto dto);
}
