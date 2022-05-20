package com.epam.esm.service.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.dto.*;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.ExceptionConstant;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.ResourceNotFoundException;
import com.epam.esm.service.validator.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    private final UserRepository userRepository;

    private final Mapper<Order, OrderDto> mapper;

    private final ModelMapper modelMapper;
    private final Validator<Pagination> paginationValidator;

    @Override
    public OrderDto findById(Long userId, Long orderId) throws IllegalAccessException {
        log.info("IN findById - userId: {}, orderId: {}", userId, orderId);
        OrderDto orderDto = findById(orderId);
        if (!orderDto.getUser().getId().equals(userId)) {
            throw new IllegalAccessException(ExceptionConstant.ACCESS_DENIED);
        }
        return orderDto;
    }

    @Override
    public OrderDto findById(Long orderId) {
        log.info("IN findById - orderId: {}", orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException(orderId, ExceptionConstant.RESOURCE_NOT_FOUND));
        return mapper.toDto(order);
    }

    @Override
    public List<OrderDto> findUserOrders(long userId, Pagination pagination) {
        log.info("IN findUserOrders - userId: {}, page: {}, pageSize: {}",
                userId, pagination.getPage(), pagination.getPageSize());

        this.paginationValidator.validateForCreate(pagination);
        return orderRepository.findUserOrders(userId, pagination).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDto create(CreateOrderDto createOrderDto) {
        log.info("IN create - order: {}", createOrderDto);
        Order order = new Order();
        Set<Certificate> certificates = createOrderDto.getCertificates().stream()
                .map(c -> modelMapper.map(c, Certificate.class))
                .collect(Collectors.toSet());
        order.setCertificates(certificates);

        Optional<User> user = userRepository.findById(createOrderDto.getUserId());
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(createOrderDto.getUserId(), ExceptionConstant.RESOURCE_NOT_FOUND);
        }
        order.setUser(user.get());

        this.calculateCost(order);
        this.orderRepository.create(order);
        return this.mapper.toDto(order);
    }

    private void calculateCost(Order order) {
        BigDecimal cost = new BigDecimal("0.00");
        Set<Certificate> certificates = new HashSet<>();
        for (Certificate c : order.getCertificates()) {
            Optional<Certificate> certificate = certificateRepository.findById(c.getId());
            if (!certificate.isPresent()) {
                throw new ResourceNotFoundException(order.getId(), ExceptionConstant.RESOURCE_NOT_FOUND);
            }
            certificates.add(certificate.get());
            cost = cost.add(certificate.get().getPrice());
        }
        order.setCertificates(certificates);
        order.setCost(cost);
    }
}
