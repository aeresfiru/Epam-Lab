package com.epam.esm.repository;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Pagination
 *
 * @author alex
 * @version 1.0
 * @since 23.04.22
 */
@Data
@AllArgsConstructor
public class Pagination {

    private Integer page;

    private Integer pageSize;
}
