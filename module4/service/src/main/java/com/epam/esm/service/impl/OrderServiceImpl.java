package com.epam.esm.service.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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
    public Order findById(Long orderId) {
        log.info("Searching by id: {}", orderId);
        return orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<Order> findUserOrders(Long userId, Pageable pageable) {
        log.info("Searching for user orders - userId: {}, pageable: {}", userId, pageable);
        return orderRepository.findAllByUserId(userId, pageable);
    }

    @Override
    @Transactional
    public Order create(Order order) {
        log.info("Creating order - order: {}", order);
        calculateCost(order);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteById(Long orderId) {
        log.info("Deleting order - orderId: {}", orderId);
        if (!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException();
        }
        orderRepository.deleteById(orderId);
    }

    private void calculateCost(Order order) {
        BigDecimal cost = new BigDecimal("0.00");
        Set<Certificate> certificates = new HashSet<>();

        for (Certificate certificate : order.getCertificates()) {
            long id = certificate.getId();
            certificate = certificateRepository.findById(id).orElseThrow(EntityNotFoundException::new);

            cost = cost.add(certificate.getPrice());
            certificates.add(certificate);
        }
        order.setCertificates(certificates);
        order.setCost(cost);
    }
}
