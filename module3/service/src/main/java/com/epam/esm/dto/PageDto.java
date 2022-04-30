package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PageDto
 *
 * @author alex
 * @version 1.0
 * @since 28.04.22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto<T> {

    private List<T> content;
    private long totalElements;
}
