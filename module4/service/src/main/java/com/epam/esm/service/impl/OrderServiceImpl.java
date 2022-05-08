package com.epam.esm.service.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.ExceptionConstant;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * OrderServiceImpl
 *
 * @author alex
 * @version 1.0
 * @since 25.04.22
 */
@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final CertificateRepository certificateRepository;

    @Override
    public Order findByUserIdAndOrderId(Long userId, Long orderId) throws IllegalAccessException {
        log.info("IN findById - userId: {}, orderId: {}", userId, orderId);
        Order order = orderRepository.findByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new IllegalAccessException(ExceptionConstant.ACCESS_DENIED);
        }
        return order;
    }

    @Override
    public Order findById(Long orderId) {
        log.info("IN findById - orderId: {}", orderId);
        return orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException(orderId, ExceptionConstant.RESOURCE_NOT_FOUND));
    }

    @Override
    public Page<Order> findUserOrders(long userId, Pageable pageable) {
        log.info("IN findUserOrders - userId: {}, pageable: {}", userId, pageable);
        return orderRepository.findAllByUserId(userId, pageable);
    }

    @Override
    @Transactional
    public Order create(Order order) {
        log.info("IN create - order: {}", order);
        calculateCost(order);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteById(Long orderId) {
        log.info("IN deleteById - orderId={}", orderId);
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException(orderId, ExceptionConstant.RESOURCE_NOT_FOUND);
        }
        orderRepository.deleteById(orderId);
    }

    @Override
    @Transactional
    public void deleteById(Long userId, Long orderId) throws IllegalAccessException {
        log.info("IN deleteById - orderId={}, userId={}", orderId, userId);
        if (orderRepository.existsById(orderId)) {
            if (orderRepository.deleteByIdAndUserId(orderId, userId) == 0) {
                throw new IllegalAccessException(ExceptionConstant.ACCESS_DENIED);
            }
        } else {
            throw new ResourceNotFoundException(orderId, ExceptionConstant.RESOURCE_NOT_FOUND);
        }
    }

    private void calculateCost(Order order) {
        BigDecimal cost = new BigDecimal("0.00");
        Set<Certificate> certificates = new HashSet<>();
        for (Certificate certificate : order.getCertificates()) {
            long id = certificate.getId();
            certificate = certificateRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id, ExceptionConstant.RESOURCE_NOT_FOUND));
            cost = cost.add(certificate.getPrice());
            certificates.add(certificate);
        }
        order.setCertificates(certificates);
        order.setCost(cost);
    }
}
