package com.epam.esm.dao.builder;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class QueryCertificateConfig {
    private Map<String, SortingType> parameterSortingTypeMap;
    private String searchQuery;
    private final String tagParam;
}
