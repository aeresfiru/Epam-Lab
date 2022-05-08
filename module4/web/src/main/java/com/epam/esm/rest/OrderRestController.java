package com.epam.esm.rest;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.service.OrderService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class OrderRestController {

    private final OrderService orderService;

    private final ModelMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable @Positive Long id) {
        OrderDto orderDto = mapper.map(orderService.findById(id), OrderDto.class);
        addSelfRelLink(orderDto);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable @Positive Long id) {
        orderService.deleteById(id);
    }

    private void addSelfRelLink(OrderDto orderDto) {
        Link link = linkTo(this.getClass()).slash(orderDto.getId()).withSelfRel();
        orderDto.add(link);
    }
}
