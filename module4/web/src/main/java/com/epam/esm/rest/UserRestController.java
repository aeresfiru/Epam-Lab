package com.epam.esm.rest;

import com.epam.esm.domain.Order;
import com.epam.esm.domain.User;
import com.epam.esm.dto.OrderCreateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.SortTypeMapConverter;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class UserRestController {

    private final OrderService orderService;

    private final UserService userService;

    private final ModelMapper mapper;

    @GetMapping
    public CollectionModel<UserDto> findAllUsers(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size,
                                                 @RequestParam(defaultValue = "-id") String sort) {
        Pageable pageable = PageRequest.of(page, size, SortTypeMapConverter.convert(sort));
        Page<UserDto> users = userService.findAll(pageable).map(u -> mapper.map(u, UserDto.class));

        users.forEach(this::addSelfRelLink);
        Link link = linkTo(methodOn(this.getClass()).findAllUsers(page, size, sort)).withSelfRel();

        return CollectionModel.of(users, link);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable @Positive Long id) {
        UserDto user = mapper.map(userService.findById(id), UserDto.class);
        this.addSelfRelLink(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/orders")
    public CollectionModel<OrderDto> findUserOrders(@PathVariable Long userId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size,
                                                    @RequestParam(defaultValue = "-id") String sort) {

        Pageable pageable = PageRequest.of(page, size, SortTypeMapConverter.convert(sort));
        Page<OrderDto> orders = orderService.findUserOrders(userId, pageable).map(o -> mapper.map(o, OrderDto.class));
        orders.forEach(this::addSelfRelLink);
        Link link = linkTo(methodOn(this.getClass())
                .findUserOrders(userId, page, size, sort)).withSelfRel();
        return CollectionModel.of(orders, link);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<OrderDto> findById(@PathVariable @Positive Long userId,
                                             @PathVariable @Positive Long orderId)
            throws IllegalAccessException {

        OrderDto order = mapper.map(orderService.findByUserIdAndOrderId(userId, orderId), OrderDto.class);
        this.addSelfRelLink(order);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<OrderDto> makeOrder(@RequestBody @Valid OrderCreateDto orderCreateDto,
                                              @PathVariable @Positive Long userId) {
        Order order = new Order();
        order.setCertificates(orderCreateDto.getCertificates());
        User user = new User();
        user.setId(userId);
        order.setUser(user);
        OrderDto dto = mapper.map(orderService.create(order), OrderDto.class);
        this.addSelfRelLink(dto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}/orders/{orderId}")
    public void deleteOrder(@PathVariable @Positive Long userId,
                            @PathVariable @Positive Long orderId) throws IllegalAccessException {
        orderService.deleteById(userId, orderId);
    }

    private void addSelfRelLink(UserDto user) {
        Link link = linkTo(this.getClass()).slash(user.getId()).withSelfRel();
        user.add(link);
    }

    private void addSelfRelLink(OrderDto orderDto) {
        Link link = linkTo(this.getClass()).slash(orderDto.getId()).withSelfRel();
        orderDto.add(link);
    }
}
