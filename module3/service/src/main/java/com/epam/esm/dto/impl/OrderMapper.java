package com.epam.esm.dto.impl;

import com.epam.esm.domain.Order;
import com.epam.esm.dto.Mapper;
import com.epam.esm.dto.OrderDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * OrderDtoMapper
 *
 * @author alex
 * @version 1.0
 * @since 23.04.22
 */
@Component
@Qualifier("orderDtoMapper")
public class OrderMapper extends Mapper<Order, OrderDto> {

    protected OrderMapper(ModelMapper mapper) {
        super(mapper);
    }

    @Override
    public Order toEntity(OrderDto dto) {
        return modelMapper.map(dto, Order.class);
    }

    @Override
    public OrderDto toDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
