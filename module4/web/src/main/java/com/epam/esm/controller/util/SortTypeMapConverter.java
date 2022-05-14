package com.epam.esm.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

/**
 * SortTypeMapConverter
 *
 * @author alex
 * @version 1.0
 * @since 27.04.22
 */
@Slf4j
public class SortTypeMapConverter {

    private SortTypeMapConverter() {
    }

    public static Sort convert(String sort) {
        log.info("Converting sort parameters to a map: " + sort);
        List<Sort.Order> orders = new ArrayList<>();

        if (sort != null) {
            String[] elements = sort.split(",");
            for (String element : elements) {
                Sort.Direction direction = Sort.Direction.ASC;
                if (element.startsWith("-")) {
                    direction = Sort.Direction.DESC;
                    element = element.substring(1);
                }
                orders.add(new Sort.Order(direction, element));
            }
        }
        return Sort.by(orders);
    }
}
