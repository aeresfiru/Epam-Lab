package com.epam.esm.dao.builder.select;

import com.epam.esm.dao.builder.SortingType;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
public abstract class SelectQueryConfig {
    protected Map<String, SortingType> parameterSortingTypeMap;
    protected String searchQuery;
}
