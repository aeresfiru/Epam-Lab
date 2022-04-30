package com.epam.esm.rest;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.repository.Pagination;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public CollectionModel<UserDto> findAllUsers(@RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        List<UserDto> users = userService.findAll(new Pagination(page, pageSize));
        users.forEach(this::addSelfRelLink);
        Link link = linkTo(methodOn(this.getClass())
                .findAllUsers(page, pageSize)).withSelfRel();
        return CollectionModel.of(users, link);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        UserDto user = userService.findById(id);
        this.addSelfRelLink(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/orders")
    public CollectionModel<OrderDto> findUserOrders(@PathVariable Long userId,
                                         @RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer pageSize) {

        List<OrderDto> orders = orderService.findUserOrders(userId, new Pagination(page, pageSize));
        orders.forEach(this::addSelfRelLink);
        Link link = linkTo(methodOn(this.getClass())
                .findUserOrders(userId, page, pageSize)).withSelfRel();
        return CollectionModel.of(orders, link);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<OrderDto> findById(@PathVariable Long userId, @PathVariable Long orderId)
            throws IllegalAccessException {

        OrderDto orderDto = orderService.findById(userId, orderId);
        this.addSelfRelLink(orderDto);
        return ResponseEntity.ok(orderDto);
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<OrderDto> makeOrder(@RequestBody OrderDto order, @PathVariable Long userId) {
        UserDto user = new UserDto();
        user.setId(userId);
        order.setUser(user);
        OrderDto orderDto = orderService.create(order);
        this.addSelfRelLink(orderDto);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}/orders/{orderId}")
    public void deleteOrder(@PathVariable Long userId, @PathVariable Long orderId) throws IllegalAccessException {
        orderService.deleteById(userId, orderId);
    }

    private void addSelfRelLink(UserDto userDto) {
        Link link = linkTo(this.getClass()).slash(userDto.getId()).withSelfRel();
        userDto.add(link);
    }

    private void addSelfRelLink(OrderDto orderDto) {
        Link link = linkTo(this.getClass()).slash(orderDto.getId()).withSelfRel();
        orderDto.add(link);
    }
}
