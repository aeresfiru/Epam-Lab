package com.epam.esm.service.impl;

import com.epam.esm.CertificatesApplication;
import com.epam.esm.domain.Order;
import com.epam.esm.dto.Mapper;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.Pagination;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.impl.UserRepositoryImpl;
import com.epam.esm.service.IncorrectParameterException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.ResourceNotFoundException;
import com.epam.esm.service.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {UserRepositoryImpl.class, CertificatesApplication.class})
@ActiveProfiles("test")
class OrderServiceImplTest {

    private static final Order ORDER_TO_CREATE = new Order();

    @Mock
    private OrderRepository orderRepository;

    @Autowired
    private Mapper<Order, OrderDto> mapper;

    private OrderService orderService;

    @Autowired
    private Validator<Pagination> paginationValidator;

    private CertificateRepository certificateRepository;

    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void setup() {
        orderService = new OrderServiceImpl(orderRepository, certificateRepository, userRepository, mapper, modelMapper, paginationValidator);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void whenFindUserOrders_thenReturnLimitedListOfTagDto(int pageSize) {
        long userId = 1;
        Pagination pagination = new Pagination(1, pageSize);

        List<Order> expected = new LinkedList<>();
        for (int i = 0; i < pageSize; i++) {
            expected.add(new Order());
        }
        when(orderRepository.findUserOrders(userId, pagination)).thenReturn(expected);

        List<OrderDto> actual = orderService.findUserOrders(userId, pagination);
        assertEquals(pageSize, actual.size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3, -4})
    void whenFindUserOrders_thenThrowIncorrectParameterExceptionForPageSize(int pageSize) {
        long userId = 1;
        Pagination pagination = new Pagination(1, pageSize);
        assertThrows(IncorrectParameterException.class,
                () -> orderService.findUserOrders(userId, pagination));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -3, -4})
    void whenFindAll_thenThrowIncorrectParameterExceptionForPageNumber(int page) {
        long userId = 1;
        Pagination pagination = new Pagination(page, 3);
        assertThrows(IncorrectParameterException.class,
                () -> orderService.findUserOrders(userId, pagination));
    }

    @Test
    void whenFindById_thenReturnTagDto() {
        long id = 1;
        when(orderRepository.findById(id)).thenReturn(Optional.of(new Order()));

        OrderDto actual = orderService.findById(id);
        assertNotNull(actual);
    }

    @Test
    void whenFindById_thenThrowEntityNotFoundException() {
        long id = -1;
        when(orderRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.findById(id));
    }
}