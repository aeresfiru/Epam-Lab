package com.epam.esm.controller.rest;

import com.epam.esm.controller.util.SortTypeMapConverter;
import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.service.model.OrderCreateModel;
import com.epam.esm.service.model.OrderModel;
import com.epam.esm.service.model.UserModel;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * OrderRestController
 *
 * @author alex
 * @version 1.0
 * @since 3.05.22
 */
@RestController
@RequestMapping("/users")
@AllArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
public class UserRestController {

    private final OrderService orderService;

    private final UserService userService;

    private final ModelMapper mapper;

    @GetMapping
    public CollectionModel<UserModel> findAllUsers(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "5") int size,
                                                   @RequestParam(defaultValue = "-id") String sort) {
        Pageable pageable = PageRequest.of(page, size, SortTypeMapConverter.convert(sort));
        Page<UserModel> users = userService.findAll(pageable).map(u -> mapper.map(u, UserModel.class));

        users.forEach(this::addSelfRelLink);
        Link link = linkTo(methodOn(this.getClass()).findAllUsers(page, size, sort)).withSelfRel();

        return CollectionModel.of(users, link);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> findById(@PathVariable @Positive Long id) {
        UserModel user = mapper.map(userService.findById(id), UserModel.class);
        this.addSelfRelLink(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/orders")
    public CollectionModel<OrderModel> findUserOrders(@PathVariable Long userId,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "5") int size,
                                                      @RequestParam(defaultValue = "-id") String sort) {

        Pageable pageable = PageRequest.of(page, size, SortTypeMapConverter.convert(sort));
        Page<OrderModel> orders = orderService.findUserOrders(userId, pageable).map(o -> mapper.map(o, OrderModel.class));
        orders.forEach(this::addSelfRelLink);
        Link link = linkTo(methodOn(this.getClass())
                .findUserOrders(userId, page, size, sort)).withSelfRel();
        return CollectionModel.of(orders, link);
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<OrderModel> makeOrder(@RequestBody @Valid OrderCreateModel orderCreateModel,
                                                @PathVariable @Positive Long userId) {
        Order order = mapper.map(orderCreateModel, Order.class);
        order.setUser(new User(userId));
        OrderModel dto = mapper.map(orderService.create(order), OrderModel.class);
        this.addSelfRelLink(dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    private void addSelfRelLink(UserModel user) {
        Link link = linkTo(this.getClass()).slash(user.getId()).withSelfRel();
        user.add(link);
    }

    private void addSelfRelLink(OrderModel orderModel) {
        Link link = linkTo(this.getClass()).slash(orderModel.getId()).withSelfRel();
        orderModel.add(link);
    }
}
