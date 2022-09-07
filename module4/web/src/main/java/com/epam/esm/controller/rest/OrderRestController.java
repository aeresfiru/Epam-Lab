package com.epam.esm.controller.rest;

import com.epam.esm.controller.security.jwt.JwtTokenProvider;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.model.OrderCreateModel;
import com.epam.esm.service.model.OrderModel;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * OrderRestController
 *
 * @author alex
 * @version 1.0
 * @since 3.05.22
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/orders")
@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
public class OrderRestController {

    private final OrderService orderService;

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    private final ModelMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<OrderModel> getById(@PathVariable @Positive Long id) {
        OrderModel orderModel = mapper.map(orderService.findById(id), OrderModel.class);
        addSelfRelLink(orderModel);
        return new ResponseEntity<>(orderModel, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<OrderModel> makeOrder(@RequestBody @Valid OrderCreateModel orderCreateModel,
                                                HttpServletRequest request) {
        Order order = mapper.map(orderCreateModel, Order.class);
        User user = getUserFromRequest(request);
        order.setUser(user);
        order = orderService.create(order);
        OrderModel dto = mapper.map(order, OrderModel.class);
        this.addSelfRelLink(dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    private User getUserFromRequest(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsername(token);
        return userService.findByUsername(username);
    }

    private void addSelfRelLink(OrderModel orderModel) {
        Link link = linkTo(this.getClass()).slash(orderModel.getId()).withSelfRel();
        orderModel.add(link);
    }
}
