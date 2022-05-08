package com.epam.esm.repository;

import com.epam.esm.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * OrderRepository
 *
 * @author alex
 * @version 1.0
 * @since 7.05.22
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByUserId(Long userId, Pageable pageable);

    Order findByIdAndUserId(Long id, Long userId);

    Integer deleteByIdAndUserId(Long id, Long userId);
}
