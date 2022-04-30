package com.epam.esm.rest;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class OrderRestController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        OrderDto orderDto = orderService.findById(id);
        addSelfRelLink(orderDto);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        orderService.deleteById(id);
    }

    private void addSelfRelLink(OrderDto orderDto) {
        Link link = linkTo(this.getClass()).slash(orderDto.getId()).withSelfRel();
        orderDto.add(link);
    }
}
