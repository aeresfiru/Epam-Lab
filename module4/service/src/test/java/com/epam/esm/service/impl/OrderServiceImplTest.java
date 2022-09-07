package com.epam.esm.service.impl;

import com.epam.esm.domain.Certificate;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private CertificateRepository certificateRepository;

    @InjectMocks
    private OrderServiceImpl service;

    private Order order;

    private Certificate certificate;

    @BeforeEach
    void setup() {
        certificate = Certificate.builder().id(1L).price(new BigDecimal("1.00")).build();
        order = new Order(1L, new User(), null, null,
                Set.of(certificate));
    }

    @DisplayName("JUnit test for create order method")
    @Test
    void givenOrderObject_whenSaveOrder_thenReturnOrderWithCalculatedCost() {
        given(certificateRepository.findById(certificate.getId())).willReturn(Optional.of(certificate));
        given(repository.save(order)).willReturn(order);

        service.create(order);

        verify(repository, times(1)).save(order);
        assertThat(order.getCost()).isNotNull();
    }

    @DisplayName("JUnit test for create order which will throw exception")
    @Test
    void givenOrderObject_whenSaveOrder_thenThrowException() {
        given(certificateRepository.findById(certificate.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(order))
                .isInstanceOf(EntityNotFoundException.class);

        verify(repository, never()).save(order);
    }

    @DisplayName("JUnit test for find order by id")
    @Test
    void givenOrderId_whenFindById_thenReturnOrderObject() {
        given(repository.findById(order.getId())).willReturn(Optional.of(order));

        Order actual = service.findById(order.getId());

        assertThat(actual).isNotNull();
    }

    @DisplayName("JUnit test for find order by id (negative scenario)")
    @Test
    void givenWrongOrderId_whenFindById_thenThrowException() {
        given(repository.findById(order.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(order.getId()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("JUnit test for find user orders")
    @Test
    void givenUserId_whenFindUserOrders_thenReturnOrdersPage() {
        long userId = 1L;
        Pageable pageable = PageRequest.of(0, 5);
        given(repository.findAllByUserId(userId, pageable))
                .willReturn(new PageImpl<>(Collections.singletonList(order)));

        Page<Order> actual = service.findUserOrders(userId, pageable);

        assertThat(actual.getContent()).isNotEmpty();
    }
}
