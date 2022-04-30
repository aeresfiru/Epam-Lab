package com.epam.esm.repository.builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * PageSettings
 *
 * @author alex
 * @version 1.0
 * @since 28.04.22
 */
@Slf4j
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageSettings {
    private int page;
    private int size;
    protected Map<String, SortType> parameterSortingTypeMap;
    protected String searchQuery;
}
