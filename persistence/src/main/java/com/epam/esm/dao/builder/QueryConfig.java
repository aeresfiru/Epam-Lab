package com.epam.esm.dao.builder;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
public abstract class QueryConfig {
    protected final Map<String, SortingType> parameterSortingTypeMap;
    protected final String searchQuery;
}
