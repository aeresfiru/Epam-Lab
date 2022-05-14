package com.epam.esm.controller.rest;

import com.epam.esm.service.OrderService;
import com.epam.esm.service.model.OrderModel;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/orders")
@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
public class OrderRestController {

    private final OrderService orderService;

    private final ModelMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<OrderModel> getById(@PathVariable @Positive Long id) {
        OrderModel orderModel = mapper.map(orderService.findById(id), OrderModel.class);
        addSelfRelLink(orderModel);
        return new ResponseEntity<>(orderModel, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable @Positive Long id) {
        orderService.deleteById(id);
    }

    private void addSelfRelLink(OrderModel orderModel) {
        Link link = linkTo(this.getClass()).slash(orderModel.getId()).withSelfRel();
        orderModel.add(link);
    }
}
