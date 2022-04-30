package com.epam.esm.service.impl;

import com.epam.esm.domain.Order;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.Mapper;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.Pagination;
import com.epam.esm.service.ExceptionConstant;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.ResourceNotFoundException;
import com.epam.esm.service.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
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
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final Mapper<Order, OrderDto> mapper;
    private final Validator<Pagination> paginationValidator;

    public OrderServiceImpl(OrderRepository orderRepository,
                            Mapper<Order, OrderDto> mapper,
                            Validator<Pagination> paginationValidator) {

        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.paginationValidator = paginationValidator;
    }

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
    public OrderDto create(OrderDto dto) {
        log.info("IN create - order: {}", dto);
        this.calculateCost(dto);
        Order order = this.mapper.toEntity(dto);
        this.orderRepository.create(order);
        return this.mapper.toDto(order);
    }

    @Override
    @Transactional
    public void deleteById(Long orderId) {
        log.info("IN deleteById - orderId={}", orderId);
        if (!orderRepository.findById(orderId).isPresent()) {
            throw new ResourceNotFoundException(orderId, ExceptionConstant.RESOURCE_NOT_FOUND);
        }
        orderRepository.delete(orderId);
    }

    @Override
    @Transactional
    public void deleteById(Long userId, Long orderId) throws IllegalAccessException {
        log.info("IN deleteById - orderId={}, userId={}", orderId, userId);
        OrderDto orderDto = findById(userId, orderId);
        if (!orderDto.getUser().getId().equals(userId)) {
            throw new IllegalAccessException(ExceptionConstant.ACCESS_DENIED);
        }
        orderRepository.delete(orderId);
    }

    private void calculateCost(OrderDto dto) {
        BigDecimal cost = new BigDecimal("0.00");
        for (CertificateDto certificateDto : dto.getCertificates()) {
            cost = cost.add(certificateDto.getPrice());
        }
        dto.setCost(cost);
    }
}
