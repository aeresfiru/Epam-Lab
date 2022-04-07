package com.epam.esm.dao.query.config;

import com.epam.esm.dao.query.SortingType;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
public abstract class SelectQueryConfig {
    protected Map<String, SortingType> parameterSortingTypeMap;
    protected String searchQuery;
}
