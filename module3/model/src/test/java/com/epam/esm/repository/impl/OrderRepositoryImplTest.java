package com.epam.esm.repository.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import com.epam.esm.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("test")
class OrderRepositoryImplTest {

    private static final User USER_1 = new User(1L);

    private static final Tag TAG_1 = new Tag(1L);
    private static final Tag TAG_2 = new Tag(2L);

    private static final Certificate CERTIFICATE_1 = new Certificate(1L);
    private static final Certificate CERTIFICATE_2 = new Certificate(1L);

    private static final Map<Certificate, Short> certificatesInFirstOrder = new HashMap<>();
    private static final Map<Certificate, Short> certificatesInToCreateOrder = new HashMap<>();

    static {
        certificatesInFirstOrder.put(CERTIFICATE_1, (short) 1);
        certificatesInToCreateOrder.put(CERTIFICATE_2, (short) 1);
        certificatesInToCreateOrder.put(CERTIFICATE_1, (short) 1);
    }

    private static final Order ORDER_1 = new Order(1L);
    private static final Order ORDER_2 = new Order(2L);

    private static final Order ORDER_TO_CREATE = new Order();
    private final OrderRepository orderRepository;

    @Autowired
    OrderRepositoryImplTest(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Test
    void whenFindById_thenReturnOrderWithThatId() {
        Optional<Order> expected = Optional.of(ORDER_1);
        Optional<Order> actual = orderRepository.findById(ORDER_1.getId());

        Assertions.assertEquals(expected, actual);
    }
}